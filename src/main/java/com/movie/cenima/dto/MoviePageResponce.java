package com.movie.cenima.dto;

import java.util.List;

public record MoviePageResponce(List<Moviesdto> movieDtos,
                                Integer pageNumber,
                                Integer pageSize,
                                long totalElements,
                                int totalPages,
                                boolean isLast) {
}
