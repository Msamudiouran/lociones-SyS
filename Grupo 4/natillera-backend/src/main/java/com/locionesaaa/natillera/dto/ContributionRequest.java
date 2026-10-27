package com.locionesaaa.natillera.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record ContributionRequest(@NotNull @Positive BigDecimal amount, @NotNull LocalDate paidOn) {}
