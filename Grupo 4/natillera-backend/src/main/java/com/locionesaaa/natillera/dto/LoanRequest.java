package com.locionesaaa.natillera.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record LoanRequest(@NotNull @Positive BigDecimal principal, @NotNull @PositiveOrZero BigDecimal annualInterestPercent) {}
