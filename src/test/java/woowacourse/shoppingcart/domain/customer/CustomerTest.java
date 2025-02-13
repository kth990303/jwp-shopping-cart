package woowacourse.shoppingcart.domain.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowacourse.auth.support.HashPasswordEncoder;
import woowacourse.shoppingcart.exception.InvalidArgumentRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CustomerTest {

    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다.")
    @Test
    void validatePassword_not_matching() {
        Customer customer =
                Customer.of("forky123", encode("forky!1234"), "forky", 26);
        assertThatExceptionOfType(InvalidArgumentRequestException.class)
                .isThrownBy(() -> customer.validatePassword(encode("kei!1234")))
                .withMessageContaining("일치");
    }

    @DisplayName("비밀번호를 변경할 수 있다.")
    @Test
    void updatePassword() {
        Customer customer =
                Customer.of("forky123", encode("forky!1234"), "forky", 26);
        EncodePassword expected = encode("forky!4321");
        Customer updated = customer.updatePassword(expected);
        assertThat(updated.getPassword()).isEqualTo(expected.getPassword());
    }

    private EncodePassword encode(String rawPassword) {
        RawPassword password = new RawPassword(rawPassword);
        PasswordEncoder passwordEncoder = new HashPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}