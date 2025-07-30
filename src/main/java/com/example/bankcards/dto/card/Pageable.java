package com.example.bankcards.dto.card;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pageable {
    @Min(value = 0)
    @NotNull
    private Integer page;
    @Min(value = 1)
    @NotNull
    private Integer size;
}
