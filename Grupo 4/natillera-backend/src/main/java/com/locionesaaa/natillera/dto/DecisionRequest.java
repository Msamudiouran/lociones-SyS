package com.locionesaaa.natillera.dto;
import com.locionesaaa.natillera.entity.LoanStatus;
import jakarta.validation.constraints.NotNull;
public record DecisionRequest(@NotNull LoanStatus status) {}
