package com.p3319.lab2.dto.product;

import com.p3319.lab2.dto.common.CoordinatesDto;
import com.p3319.lab2.entity.enums.UnitOfMeasure;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotNull(message = "Координаты обязательны")
    @Valid
    private CoordinatesDto coordinates;

    @NotNull(message = "Единица измерения обязательна")
    private UnitOfMeasure unitOfMeasure;

    @NotNull(message = "Производитель обязателен")
    @Positive(message = "Идентификатор производителя должен быть больше 0")
    private Long manufacturerId;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть больше 0")
    private Float price;

    private Double manufactureCost; // Can be null

    @NotNull(message = "Рейтинг обязателен")
    @Positive(message = "Рейтинг должен быть больше 0")
    private Double rating;

    @NotBlank(message = "Партийный номер не может быть пустым")
    @Size(min = 28, max = 84, message = "Длина партийного номера должна быть от 28 до 84 символов")
    private String partNumber;

    @NotNull(message = "Владелец обязателен")
    @Positive(message = "Идентификатор владельца должен быть больше 0")
    private Long ownerId;
}
