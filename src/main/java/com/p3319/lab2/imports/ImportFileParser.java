package com.p3319.lab2.imports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.p3319.lab2.exception.ImportFormatException;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Component
public class ImportFileParser {

    public static final int MAX_ITEMS = 1024;

    private static final String RESOURCE_TYPE_KEY = "resourceType";
    private static final String ITEMS_KEY = "items";

    private final JsonMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();

    public ImportFile parse(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ImportFormatException("Файл пуст");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase(Locale.ROOT).endsWith(".json")) {
            throw new ImportFormatException("Допускаются только файлы с расширением .json");
        }

        JsonNode root = readRoot(file);
        if (!root.isObject()) {
            throw new ImportFormatException("Ожидался JSON-объект с ключами resourceType и items");
        }

        List<String> extraKeys = root.propertyNames().stream()
                .filter(key -> !key.equals(RESOURCE_TYPE_KEY) && !key.equals(ITEMS_KEY))
                .toList();
        if (!extraKeys.isEmpty()) {
            throw new ImportFormatException("Лишние ключи верхнего уровня: " + String.join(", ", extraKeys));
        }

        ImportResourceType resourceType = readResourceType(root);
        List<JsonNode> items = readItems(root);
        return new ImportFile(resourceType, items);
    }

    private JsonNode readRoot(MultipartFile file) {
        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            throw new ImportFormatException("Не удалось прочитать файл");
        }

        JsonNode root;
        try {
            root = mapper.readTree(content);
        } catch (JacksonException e) {
            throw new ImportFormatException("Файл не является корректным JSON");
        }

        if (root == null || root.isMissingNode()) {
            throw new ImportFormatException("Файл не является корректным JSON");
        }
        return root;
    }

    private ImportResourceType readResourceType(JsonNode root) {
        JsonNode node = root.get(RESOURCE_TYPE_KEY);
        if (node == null) {
            throw new ImportFormatException("Не указан ключ resourceType");
        }

        ImportResourceType type = node.isString() ? ImportResourceType.fromValue(node.stringValue()) : null;
        if (type == null) {
            String allowed = Arrays.stream(ImportResourceType.values())
                    .map(ImportResourceType::getValue)
                    .collect(Collectors.joining(", "));
            throw new ImportFormatException("Неизвестный resourceType. Допустимые значения: " + allowed);
        }
        return type;
    }

    private List<JsonNode> readItems(JsonNode root) {
        JsonNode node = root.get(ITEMS_KEY);
        if (node == null || !node.isArray()) {
            throw new ImportFormatException("Ключ items должен быть массивом записей");
        }
        if (node.isEmpty()) {
            throw new ImportFormatException("В файле нет записей: массив items пуст");
        }
        if (node.size() > MAX_ITEMS) {
            throw new ImportFormatException(
                    "Допускается не более " + MAX_ITEMS + " записей, в файле их " + node.size());
        }

        List<JsonNode> items = new ArrayList<>(node.size());
        for (JsonNode item : node) {
            items.add(item);
        }
        return items;
    }
}
