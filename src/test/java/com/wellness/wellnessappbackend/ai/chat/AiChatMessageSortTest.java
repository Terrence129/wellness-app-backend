package com.wellness.wellnessappbackend.ai.chat;

import com.wellness.wellnessappbackend.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AiChatMessageSortTest {

    @Test
    void defaultsToCreatedAtAscending() {
        Sort sort = AiChatMessageSort.parse(null);

        Sort.Order order = sort.getOrderFor("createdAt");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC);
        assertThat(AiChatMessageSort.describe(sort)).containsExactly("createdAt,asc");
    }

    @Test
    void rejectsUnsupportedField() {
        assertThatThrownBy(() -> AiChatMessageSort.parse("role,asc"))
                .isInstanceOf(ApiException.class)
                .hasMessage("Unsupported sort field");
    }
}
