package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Содержит номер страницы и её размер
 */
@Schema(description = "Пагинация")
@Getter
@Setter
@AllArgsConstructor
public class Pageable {
    @Schema(description = "Номер страницы", example = "0")
    @Min(value = 0)
    @NotNull
    private Integer page;
    @Schema(description = "Размер страницы", example = "10")
    @Min(value = 1)
    @NotNull
    private Integer size;
}
