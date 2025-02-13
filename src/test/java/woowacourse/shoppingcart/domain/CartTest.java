package woowacourse.shoppingcart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowacourse.shoppingcart.exception.ExistCartItemException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CartTest {
    private final CartItem cartItem =
            CartItem.of(1L, "감자", 200, "potato.jpg", 2);
    private final Cart cart = new Cart(Collections.emptyList());

    @DisplayName("카트가 올바르게 생성된다.")
    @Test
    void constructor() {
        assertThat(cart).isNotNull();
    }

    @DisplayName("카트에 상품을 담는다.")
    @Test
    void add() {
        cart.add(cartItem);

        assertThat(cart.getCartItems())
                .contains(cartItem);
    }

    @DisplayName("카트에 이미 담긴 상품을 담으려하면 예외를 발생시킨다.")
    @Test
    void add_duplicateItem() {
        cart.add(cartItem);

        assertThatExceptionOfType(ExistCartItemException.class)
                .isThrownBy(() -> cart.add(cartItem))
                .withMessageContaining("이미");
    }

    @DisplayName("카트 상품 수량을 변경한다.")
    @Test
    void update() {
        cart.add(cartItem);

        cart.updateCartItem(cartItem, 2);

        CartItem actual = cart.getCartItems().get(0);
        assertThat(actual.getQuantity()).isEqualTo(2);
    }

    @DisplayName("카트의 상품 일부를 제거한다.")
    @Test
    void delete() {
        cart.add(cartItem);

        cart.delete(cartItem);

        assertThat(cart.getCartItems().size()).isEqualTo(0);
    }
}
