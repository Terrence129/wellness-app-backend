package com.wellness.wellnessappbackend.common;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public record PageResponse<T>(
        List<T> content,
        PageMetadata page
) {
    public static <T> PageResponse<T> from(Page<T> source, List<String> sort) {
        return new PageResponse<>(
                source.getContent(),
                new PageMetadata(
                        source.getNumber(),
                        source.getSize(),
                        source.getTotalElements(),
                        source.getTotalPages(),
                        sort
                )
        );
    }

    public record PageMetadata(
            int number,
            int size,
            long totalElements,
            int totalPages,
            List<String> sort
    ) {
    }
}
