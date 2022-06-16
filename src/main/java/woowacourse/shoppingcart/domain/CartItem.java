package woowacourse.shoppingcart.domain;

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static CartItem of(Long id, String name, int price, String thumbnail, int quantity) {
        return new CartItem(new Product(id, name, price, thumbnail), quantity);
    }

    public CartItem updateQuantity(int updatedQuantity) {
        quantity = updatedQuantity;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
