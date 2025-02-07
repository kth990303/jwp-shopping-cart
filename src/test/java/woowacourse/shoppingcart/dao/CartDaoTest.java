package woowacourse.shoppingcart.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.exception.InvalidCartItemException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:test_schema.sql", "classpath:data.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CartDaoTest {
    private final CartDao cartDao;
    private final ProductDao productDao;
    private final JdbcTemplate jdbcTemplate;

    private static final String CART_TEST_USERNAME = "puterism";

    public CartDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        cartDao = new CartDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        productDao.save(new Product("banana", 1_000, "woowa1.com"));
        productDao.save(new Product("apple", 2_000, "woowa2.com"));

        cartDao.addCartItem(CART_TEST_USERNAME, 1L);
        cartDao.addCartItem(CART_TEST_USERNAME, 2L);
    }

    @DisplayName("장바구니가 올바르게 생성된다.")
    @Test
    void addCart() {
        Cart cart = new Cart(cartDao.findCartItemsByCustomerUsername(CART_TEST_USERNAME));

        assertThat(cart.getCartItems()).hasSize(2);
    }

    @DisplayName("구매자 username로 해당 구매자가 담은 장바구니 아이템 목록을 가져온다.")
    @Test
    void findCartsByCustomerId() {
        // when
        final List<CartItem> cartItems = cartDao.findCartItemsByCustomerUsername(CART_TEST_USERNAME);

        // then
        assertAll(
                () -> assertThat(cartItems.get(0).getProduct().getName()).isEqualTo("banana"),
                () -> assertThat(cartItems.get(1).getProduct().getName()).isEqualTo("apple")
        );
    }

    @DisplayName("구매자 username로 해당 구매자가 담은 장바구니 상품 id 목록을 가져온다.")
    @Test
    void findProductIdsByCustomerId() {
        // when
        final List<Long> productIds = cartDao.findProductIdsByCustomerUsername(CART_TEST_USERNAME);

        // then
        assertThat(productIds).containsAll(List.of(1L, 2L));
    }

    @DisplayName("구매자 username과 상품 id로 장바구니에 담긴 상품 정보를 가져온다.")
    @Test
    void findCart() {
        // when
        CartItem cartItem = cartDao.findCartItemByProductId(1L, CART_TEST_USERNAME)
                .orElseThrow(InvalidCartItemException::new);

        // then
        assertAll(
                () -> assertThat(cartItem.getProduct().getName()).isEqualTo("banana"),
                () -> assertThat(cartItem.getQuantity()).isEqualTo(1)
        );
    }

    @DisplayName("장바구니에 상품을 성공적으로 담는다.")
    @Test
    void addCartItem() {
        // given
        final Long productId = productDao.save(new Product("kiwi", 3_000, "woowakiwi.com"));

        // when
        final Long actual = cartDao.addCartItem(CART_TEST_USERNAME, productId);

        // then
        assertThat(actual).isEqualTo(productId);
    }

    @DisplayName("장바구니 상품의 수량을 변경한다.")
    @Test
    void updateCartItemQuantity() {
        cartDao.updateCartItemQuantity(3, 2L, CART_TEST_USERNAME);

        final List<CartItem> cartItems = cartDao.findCartItemsByCustomerUsername(CART_TEST_USERNAME);

        assertThat(cartItems.get(1).getQuantity()).isEqualTo(3);
    }

    @DisplayName("장바구니 아이템을 성공적으로 삭제한다.")
    @Test
    void deleteCartItem() {
        // given
        final Long productId = productDao.save(new Product("kiwi", 3_000, "woowakiwi.com"));
        cartDao.addCartItem(CART_TEST_USERNAME, productId);

        // when
        cartDao.deleteCartItem(List.of(productId), CART_TEST_USERNAME);
        final List<Long> productIds = cartDao.findProductIdsByCustomerUsername(CART_TEST_USERNAME);

        // then
        assertThat(productIds).doesNotContain(productId);
    }

    @DisplayName("장바구니를 성공적으로 비운다.")
    @Test
    void deleteCart() {
        // when
        cartDao.deleteAllCartItems(CART_TEST_USERNAME);
        final List<CartItem> actual = cartDao.findCartItemsByCustomerUsername("puterism");

        // then
        assertThat(actual.size()).isEqualTo(0);
    }
}
