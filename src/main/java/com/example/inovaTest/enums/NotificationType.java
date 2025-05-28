package com.example.inovaTest.enums;

public enum NotificationType {
    FRIENDSHIP_REQUEST("Solicitação de amizade"),
    FRIENDSHIP_ACCEPTED("Amizade aceita"),
    NEW_LIKE("Curtida Post"),
    NEW_COMMENT("Novo comentário");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}