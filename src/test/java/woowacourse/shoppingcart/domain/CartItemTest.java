package woowacourse.shoppingcart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemTest {

    @DisplayName("아이템의 수량을 변경시킨다.")
    @Test
    void updateQuantity() {
        CartItem cartItem = CartItem.of(1L, "감자", 200, "potato.jpg", 3);
        int expectedQuantity = 2;

        cartItem.updateQuantity(expectedQuantity);

        assertThat(cartItem.getQuantity()).isEqualTo(expectedQuantity);
    }
}
