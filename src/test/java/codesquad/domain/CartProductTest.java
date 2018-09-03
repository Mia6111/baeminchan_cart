package codesquad.domain;

import codesquad.support.PriceCalcultor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
public class CartProductTest {
    private Product product;
    private CartProduct cartProduct;
    private Cart cart;
    private PriceCalcultor priceCalcultor = new PriceCalcultor();

    private SoftAssertions softly;
    @Before
    public void setUp(){
        product = Product.builder().id(1L).price(1000L).discountRate(PriceCalcultor.NO_DISCOUNT_THRESHOLD - 10L).build();
        cart = new Cart();
        cartProduct = CartProduct.builder().cart(cart).product(product).count(PriceCalcultor.DISCOUNT_AMT_THRESHOLD + 40).build();
        cart.addProduct(cartProduct);

        softly = new SoftAssertions();

    }
    @Test
    public void test_기존없는_상품_장바구니추가(){
        int originalCnt = cart.getCartProductCnt();
        Product notDuplicateProduct =  Product.builder().id(2L).price(2000L).discountRate(PriceCalcultor.NO_DISCOUNT_THRESHOLD - 10L).build();

        CartProduct notDuplicate = CartProduct.builder()
                .product(notDuplicateProduct)
                .count(10)
                .build();
        cart.addProduct(notDuplicate);

        softly.assertThat(cart.getCartProductCnt()).isEqualTo( originalCnt+1 );
        softly.assertThat(cart.getCartProducts()
                .stream()
                .filter(x -> x.equals(notDuplicate))
                .findFirst()
                .get()
                .getCount())
        .isEqualTo(notDuplicate.getCount());
        softly.assertAll();
    }
    @Test
    public void test_이미존재하는_상품_장바구니추가(){
        int originalCnt = cart.getCartProductCnt();
        int originalCartProductCnt = cartProduct.getCount();
        CartProduct duplicate = CartProduct.builder()
                .id(cart.getId())
                .cart(cart)
                .product(product)
                .count(10)
                .build();
        cart.addProduct(duplicate);

        softly.assertThat(cart.getCartProductCnt()).isEqualTo(originalCnt);
        softly.assertThat(cart.getCartProducts()
                .stream()
                .filter(x -> x.equals(duplicate))
                .findFirst()
                .get()
                .getCount())
                .isEqualTo(duplicate.getCount() + originalCartProductCnt);
        softly.assertAll();
    }
    @Test
    public void test_가격정보_Map반환확인_할인적용(){

        Map<String, Long> calculation = cart.getCalculation();


        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(calculation.get("totalPrice")).isGreaterThanOrEqualTo(PriceCalcultor.DELIVERY_FEE_FREE_THRESHOLD).as("40000원 이상 구매 확이");
        log.debug("formmatedMoney {} , {}", calculation.get("formattedTotalPrice"), calculation.get("formattedDeliveryTotalPrice"));
        softly.assertAll();
    }

}
