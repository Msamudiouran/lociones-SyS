package com.locionesaaa.natillera.dto;
import jakarta.validation.constraints.*;
public record CreateMemberRequest(@NotBlank String fullName, @NotBlank String document, @Size(max=30) String phone) {}
