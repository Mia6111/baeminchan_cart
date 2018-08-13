package codesquad.domain;

import codesquad.dto.CartProductDTO;
import codesquad.support.AbstractEntity;
import codesquad.support.PriceCalcultor;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter @NoArgsConstructor
@Entity
@Slf4j
public class CartProduct extends AbstractEntity{
    @ManyToOne(optional = false)
    @NotNull
    @ToString.Exclude
    private Cart cart;

    @ManyToOne(optional = false)
    @NotNull
    @ToString.Exclude
    private Product product;

    @Column(nullable = false)
    int count = 1;

    @Transient
    @DecimalMin(value = "0")
    Long totalPrice = 0L;

    @Builder
    public CartProduct(CartProductDTO dto){
        this.product = dto.getProduct();
        this.count = dto.getCount();
        this.totalPrice = dto.getTotalPrice();
        this.registerCart(dto.getCart());

    }
    public void registerCart(@NotNull Cart cart){
        this.cart = cart;
        this.cart.addCartProduct(this);
    }

    @PostLoad
    @PrePersist @PreUpdate
    public void initTotalPrice() {
        this.totalPrice =  product.calculatePrice(PriceCalcultor.getInstance(), this.count);
        log.debug(" initTotalPrice called CartProduct {} ",totalPrice);
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void changeCountBy(CartProduct cartProduct) {
        this.count += cartProduct.count;
    }
}
