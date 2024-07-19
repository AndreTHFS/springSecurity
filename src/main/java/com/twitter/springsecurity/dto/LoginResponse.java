package com.twitter.springsecurity.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
