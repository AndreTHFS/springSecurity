package com.twitter.springsecurity.dto;

import java.util.List;

public record FeedDto(List<FeedItemDto> feedItens, int pag, int pageSize, int totalPages, long totalElements) {
}
