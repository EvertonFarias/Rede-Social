package com.example.inovaTest.enums;

public enum GenderRole {
    MALE,
    FEMALE,
    OTHER;

    public static GenderRole fromString(String value) {
        if (value == null || value.isBlank()) {
            return GenderRole.OTHER; // ou lançar exceção, se preferir
        }
        try {
            return GenderRole.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor de gênero inválido: " + value);
        }
}
}
