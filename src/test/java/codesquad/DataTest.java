package codesquad;

import codesquad.domain.*;
import codesquad.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
public class DataTest {
    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    private Cart cart;
    private Cart notOwnedCart;
    private CartProduct cartProduct;
    private Product product;

    @Before
    public void setUp() {

        product = Product.builder().id(1L).price(1000L).discountRate(0L).build();
        product = productRepository.saveAndFlush(product);
        cart = new Cart(null, 0);
        cart = cartRepository.saveAndFlush(cart);
        notOwnedCart = new Cart(null, 0);
        notOwnedCart = cartRepository.saveAndFlush(cart);

        cartProduct = CartProduct.builder().cart(cart).product(product).build();
        cartProduct = cartProductRepository.saveAndFlush(cartProduct);

    }

    @Test
    public void test_findByCartIdAndId_성공() {
        assertThat(cartProductRepository.findByCartAndId(cart, cartProduct.getId()).isPresent()).isTrue();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_findByCartIdAndId_실패() {
        assertThat(cartProductRepository.findByCartAndId(notOwnedCart, cartProduct.getId()));
    }

    @Test
    public void test_findByCartAndProduct_성공() {
        assertThat(cartProductRepository.findByCartAndProduct(cart, product).isPresent()).isTrue();

    }

    @Test(expected = ResourceNotFoundException.class)
    public void test_findByCartAndProduct_실패() {
        assertThat(cartProductRepository.findByCartAndProduct(cart, product));
    }
}
