package com.shagtv.contentcalendar.dto;

public record TokerResponce(
        String token,
        String refreshToken,
        String[] roles
) {
}
