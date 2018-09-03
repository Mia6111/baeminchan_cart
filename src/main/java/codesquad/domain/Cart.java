package codesquad.domain;

import codesquad.dto.CartProductDTO;
import codesquad.support.PriceCalcultor;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.*;


@Entity
@Slf4j
@Getter @NoArgsConstructor
public class Cart {
    public static final Cart EMPTY_CART = new EmptyCart(null, 0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @OneToMany(mappedBy = "cart")
    private List<CartProduct> cartProducts = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    private User user;

    //todo  ProductService - List<cartItems>의 갯수가 0이 되면, Cart 삭제, CartHistory Insert

    @Transient
    private int cartProductCnt;


    public Cart(User user, int cartProductCnt) {
        this.user = user;
        this.cartProductCnt = cartProductCnt;
    }

    @PostLoad @PostPersist @PostUpdate
    public void calculateCartProductCnt() {
        this.cartProductCnt = cartProducts.size();
        log.debug("getCartProductCnt {}", cartProductCnt);
    }
    public void increaseCartProductCnt() {
        this.cartProductCnt++;
        log.debug("increaseCartProductCnt {}", this.cartProductCnt);
    }
    public void decreateCartProductCnt() {
        this.cartProductCnt--;
        log.debug("decreateCartProductCnt {}", this.cartProductCnt);
    }
    @JsonGetter("calculation")
    public Map getCalculation() {
        Map<String, Object> calculation = new HashMap();
        Long totalPrice = 0L;
        for (CartProduct cartProduct :cartProducts) {
            totalPrice += cartProduct.getTotalPrice();
        }
        Long deliveryFee = PriceCalcultor.calculateDeliveryFee(totalPrice);
        Long deliveryTotalPrice = totalPrice + deliveryFee;

        calculation.put("totalPrice",totalPrice);
        calculation.put("deliveryFee", deliveryFee);
        calculation.put("deliveryFeeThreashold", PriceCalcultor.DELIVERY_FEE_FREE_THRESHOLD);
        calculation.put("deliveryTotalPrice",deliveryTotalPrice);
        return calculation;
    }
    @JsonIgnore
    public boolean isEmptyCart() {
        return false;
    }

    public void assignOwner(User loginUser){
        this.user = loginUser;
    }

    public boolean isOwner(User user) {
        return this.user.equals(user);
    }

    public void addProduct(CartProduct cartProduct){
        if (cartProducts.contains(cartProduct)) {
            cartProducts.stream().filter(x -> x.equals(cartProduct))
                    .findFirst()
                    .ifPresent(x -> x.addProduct(cartProduct.count));
            return;
        }
        cartProducts.add(cartProduct);
        this.increaseCartProductCnt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(user, cart.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    private static class EmptyCart extends Cart {
        public EmptyCart(User user, int cartProductCnt) {
            super(user, cartProductCnt);
        }

        @Override
        public boolean isEmptyCart() {
            return true;
        }

        @Override
        public Map getCalculation() {
            Map<String, Object> calculation = new HashMap();
            calculation.put("totalPrice",0);
            calculation.put("deliveryFee", 0);
            calculation.put("deliveryFeeThreashold", PriceCalcultor.DELIVERY_FEE_FREE_THRESHOLD);
            calculation.put("deliveryTotalPrice",0);
            return calculation;

        }
    }

}
