package com.p3319.lab2.imports;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.p3319.lab2.exception.ImportRejectedException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.exc.PropertyBindingException;
import tools.jackson.databind.exc.UnrecognizedPropertyException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.LogicalType;

@Component
@RequiredArgsConstructor
public class ImportItemReader {

    private final Validator validator;

    private final JsonMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
            .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
            .withCoercionConfig(LogicalType.Textual, config -> config
                    .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                    .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                    .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail))
            .build();

    public <T> T read(JsonNode item, Class<T> type, int index) {
        if (!item.isObject()) {
            throw reject(index, "", "ожидается JSON-объект");
        }

        checkNestedIds(item, "", index);

        T dto;
        try {
            dto = mapper.treeToValue(item, type);
        } catch (JacksonException e) {
            throw bindingError(e, index);
        }

        validate(dto, index);
        return dto;
    }

    private void checkNestedIds(JsonNode node, String path, int index) {
        for (Map.Entry<String, JsonNode> property : node.properties()) {
            JsonNode value = property.getValue();
            if (!value.isObject()) {
                continue;
            }
            String childPath = path.isEmpty() ? property.getKey() : path + "." + property.getKey();
            if (value.has("id")) {
                throw reject(index, childPath + ".id", "неизвестное поле");
            }
            checkNestedIds(value, childPath, index);
        }
    }

    private <T> void validate(T dto, int index) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        violations.stream()
                .min(Comparator.comparing((ConstraintViolation<T> violation) -> violation.getPropertyPath().toString())
                        .thenComparing(ConstraintViolation::getMessage))
                .ifPresent(violation -> {
                    throw reject(index, violation.getPropertyPath().toString(), violation.getMessage());
                });
    }

    private ImportRejectedException bindingError(JacksonException e, int index) {
        String field = path(e);

        if (e instanceof UnrecognizedPropertyException) {
            return reject(index, field, "неизвестное поле");
        }
        if (e instanceof MismatchedInputException mismatched && mismatched.getTargetType() != null) {
            Class<?> target = mismatched.getTargetType();
            if (target.isEnum()) {
                return reject(index, field, "недопустимое значение. Допустимые: " + enumValues(target));
            }
            return reject(index, field, "неверный тип значения, " + expected(target));
        }
        return reject(index, field, "некорректное значение");
    }

    private String path(JacksonException e) {
        StringBuilder path = new StringBuilder();
        for (JacksonException.Reference reference : e.getPath()) {
            if (reference.getPropertyName() != null) {
                if (!path.isEmpty()) {
                    path.append('.');
                }
                path.append(reference.getPropertyName());
            } else if (reference.getIndex() >= 0) {
                path.append('[').append(reference.getIndex()).append(']');
            }
        }

        if (e instanceof PropertyBindingException binding && binding.getPropertyName() != null) {
            String name = binding.getPropertyName();
            if (!path.toString().equals(name) && !path.toString().endsWith("." + name)) {
                if (!path.isEmpty()) {
                    path.append('.');
                }
                path.append(name);
            }
        }
        return path.toString();
    }

    private String enumValues(Class<?> type) {
        return Arrays.stream(type.getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    private String expected(Class<?> type) {
        if (type == Integer.class || type == int.class || type == Long.class || type == long.class) {
            return "ожидается целое число";
        }
        if (Number.class.isAssignableFrom(type) || type == float.class || type == double.class) {
            return "ожидается число";
        }
        if (type == String.class) {
            return "ожидается строка";
        }
        return "ожидается объект";
    }

    private ImportRejectedException reject(int index, String field, String message) {
        String place = field.isBlank() ? "" : ", поле " + field;
        return new ImportRejectedException("Запись " + index + place + ": " + message);
    }
}
