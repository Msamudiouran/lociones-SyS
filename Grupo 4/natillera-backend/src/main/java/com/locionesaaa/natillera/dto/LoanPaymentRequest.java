package com.locionesaaa.natillera.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record LoanPaymentRequest(@NotNull @Positive BigDecimal amount, @NotNull LocalDate paidOn) {}
