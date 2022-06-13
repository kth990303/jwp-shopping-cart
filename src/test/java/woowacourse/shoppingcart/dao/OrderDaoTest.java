package woowacourse.shoppingcart.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:test_schema.sql", "classpath:data.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final OrderDao orderDao;

    public OrderDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderDao = new OrderDao(jdbcTemplate);
    }

    @DisplayName("Order를 추가하는 기능")
    @Test
    void addOrders() {
        //given
        final String customerUsername = "kth990303";

        //when
        final Long orderId = orderDao.addOrders(customerUsername);

        //then
        assertThat(orderId).isNotNull();
    }

    @DisplayName("CustomerId 집합을 이용하여 OrderId 집합을 얻는 기능")
    @Test
    void findOrderIdsByCustomerId() {
        //given
        final String customerUsername = "kth990303";
        jdbcTemplate.update("INSERT INTO ORDERS (customer_username) VALUES (?)", customerUsername);
        jdbcTemplate.update("INSERT INTO ORDERS (customer_username) VALUES (?)", customerUsername);

        //when
        final List<Long> orderIdsByCustomerId = orderDao.findOrderIdsByCustomerUsername(customerUsername);

        //then
        assertThat(orderIdsByCustomerId).hasSize(2);
    }

}
