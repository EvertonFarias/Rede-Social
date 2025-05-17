package com.example.inovaTest.dtos.auth;

import java.util.Date;

public record RegisterDTO(String login, String password, String email, String gender, Date birthday ) {
}
