package com.wellness.wellnessappbackend.ai.advice;

import com.wellness.wellnessappbackend.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AiAdviceSortTest {

    @Test
    void defaultsToCreatedAtDescending() {
        Sort sort = AiAdviceSort.parse(null);

        Sort.Order order = sort.getOrderFor("createdAt");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
        assertThat(AiAdviceSort.describe(sort)).containsExactly("createdAt,desc");
    }

    @Test
    void rejectsUnsupportedField() {
        assertThatThrownBy(() -> AiAdviceSort.parse("modelName,desc"))
                .isInstanceOf(ApiException.class)
                .hasMessage("Unsupported sort field");
    }
}
