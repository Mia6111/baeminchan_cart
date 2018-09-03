package codesquad.service;

import codesquad.domain.*;
import codesquad.dto.CartProductDTO;
import codesquad.support.PriceCalcultor;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartServiceTest {
    Logger log = LoggerFactory.getLogger(CartServiceTest.class);

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductService cartService;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private ProductRepository productRepository;

    CartProductDTO dto_product1, dto_product2;
    Product product1, product2;
    User user;
    Cart cartOfloginUser;
    @Before
    public void setUp() throws Exception {
        product1 = Product.builder().id(1L).title("TEST Product1").price(1000L).discountRate(0L).build();
        product2 = Product.builder().id(2L).title("TEST Product2").price(2000L).discountRate(10L).build();
        dto_product1 = CartProductDTO.builder().count(3).productId(product1.getId()).build();
        dto_product2 = CartProductDTO.builder().count(6).productId(product2.getId()).build();

        productRepository.save(product1);
        productRepository.save(product2);

        user = User.builder().id(1L).email("javajigi@tech.com").phoneNumber("12345678").name( "javajigi").phoneNumber( "010-1234-5678").build();
        cartOfloginUser = new Cart(user, 0);

        cartOfloginUser = cartRepository.save(cartOfloginUser);

        CartProduct cartProduct = CartProduct.builder().cart(cartOfloginUser).product(product1).count(PriceCalcultor.DISCOUNT_AMT_THRESHOLD + 40).build();

        cartProductRepository.save(cartProduct);

    }

    /*
    TEST
    - 비회원 장바구니 있을때, 로그인 시 유지
    - 비회원 -> 회원으로 바꿨을 때, 회원의 기존 장바구니 가져오기

     */

    @Test
    public void test_로그인시(){
        Cart cart = cartService.getCartofLoginUser(user, Cart.EMPTY_CART);

        assertThat(cart.isEmptyCart()).isFalse();
        assertThat(cart.getId()).isEqualTo(cartOfloginUser.getId());
    }
    @Test
    public void addCartProduct_비회원_장바구니있고_로그인시_유지_기존장바구니없는경우() {

    }

    public void addCartProduct_회원_로그인시_기존장바구니_있는경우() {

    }
    public List<CartProduct> extractCartProduct(Collection<CartProduct> cartProducts, Long productId){
        return cartProducts.stream().filter(x -> x.getProduct().getId().equals(productId)).collect(Collectors.toList());
    }
    @Test
    public void streamTest() {

        log.debug("arrays {}",
                Arrays.asList(CartProduct.builder().count(1).build()).stream().map(x -> x.getId()).collect(Collectors.toList()));
    }
}
