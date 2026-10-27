package com.locionesaaa.natillera.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record CreateCycleRequest(@NotBlank String name, @NotNull LocalDate openingDate, @NotNull @Positive BigDecimal fixedContribution) {}
