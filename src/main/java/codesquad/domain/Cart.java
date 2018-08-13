package codesquad.domain;

import codesquad.support.AbstractEntity;
import codesquad.support.PriceCalcultor;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.*;


@Entity
@Slf4j
@Data
public class Cart extends AbstractEntity {
    public static final Cart EMPTY_CART = new EmptyCart();

    @JsonIgnore
    @OneToMany(mappedBy = "cart")
    @Cascade(CascadeType.ALL)
    @OrderBy("created_at")
    private List<CartProduct> cartProducts = new ArrayList<>();
    //private Map<Long, CartProduct> cartProductsMap = new LinkedHashMap<>();

    @JsonIgnore
    @OneToOne
    private User user;

    //todo  ProductService - List<cartItems>의 갯수가 0이 되면, Cart 삭제, CartHistory Insert

    @Transient
    private int cartProductCnt;

    @JsonIgnore
    public boolean isEmptyCart() {
        return false;
    }


    @PostLoad @PostPersist @PostUpdate
    public void setCartProductCnt() {
        this.cartProductCnt = cartProducts.size();
        log.debug("getCartProductCnt {}", cartProductCnt);
    }

    //todo 어떻게 하나
    //@JsonAnyGetter
    @JsonGetter("calculation")
    public Map getCalculation() {
        Map<String, Object> calculation = new HashMap();
        Long totalPrice = 0L;
        for (CartProduct cartProduct :cartProducts) {
            totalPrice += cartProduct.getTotalPrice();
        }
         Long deliveryFee = new PriceCalcultor().calculateDeliveryFee(totalPrice);
        Long deliveryTotalPrice = totalPrice + deliveryFee;
        calculation.put("totalPrice",totalPrice);
        calculation.put("deliveryFee", deliveryFee);
        calculation.put("deliveryFeeThreashold", PriceCalcultor.DELIVERY_FEE_FREE_THRESHOLD);
        calculation.put("deliveryTotalPrice",deliveryTotalPrice);
        return calculation;
    }

    public void addCartProduct(CartProduct cartProduct){
        log.debug("cartProduct {} {}", cartProduct, cartProduct.hashCode());
        //todo 리팩토링 - 코드 정리 또는 List > Map 으로 바꾸기
        //hint 가정 : id가 이미 있는, Repository에 저장된 Product여야한다...
        Optional<CartProduct> duplicate = cartProducts.stream()
                .filter(x -> x.getProduct().getId().equals(cartProduct.getProduct().getId())).findFirst();
        if (duplicate.isPresent()){
            log.debug(" isPresent {}", duplicate.get());
            duplicate.get().changeCountBy(cartProduct);
            log.debug("isPresent cartProduct {} {}", cartProduct, cartProduct.hashCode());
            return;
        }
        this.cartProducts.add(cartProduct);
        cartProductCnt++;
    }

    public boolean isOwner(User user) {
        return this.user.equals(user);
    }

    private static class EmptyCart extends Cart {
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
