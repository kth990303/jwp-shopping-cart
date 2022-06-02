package woowacourse.shoppingcart.domain.customer;

import java.util.regex.Pattern;

public class Password {
    private static final Pattern PASSWORD_REGEX =
            Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^*])[a-zA-Z0-9!@#$%^*]{8,20}$");

    private final String password;

    public Password(String password) {
        validatePassword(password);
        this.password = password;
    }

    public boolean hasSamePassword(String password) {
        return this.password.equals(password);
    }

    private void validatePassword(String password) {
        if (password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 공백일 수 없습니다.");
        }
        if (!PASSWORD_REGEX.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호는 알파벳, 숫자, 특수문자를 포함한 8자 이상 20자 이하여야 합니다.");
        }
    }

    public String getPassword() {
        return password;
    }
}
