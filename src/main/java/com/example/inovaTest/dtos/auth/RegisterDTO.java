package com.example.inovaTest.dtos.auth;

import java.time.LocalDate;

public record RegisterDTO(String login, String password, String email, String gender, LocalDate  dateOfBirth ) {
}
