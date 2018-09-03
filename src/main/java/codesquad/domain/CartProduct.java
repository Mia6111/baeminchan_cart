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
@ToString
public class CartProduct{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    public CartProduct(long id, Cart cart, Product product, int count){
        this.id = id;
        this.product = product;
        this.cart = cart;
        this.count = count;
        this.totalPrice = product.calculatePrice(count);
    }

    @PostLoad @PostPersist @PostUpdate
    public void resetTotalPrice() {
        this.totalPrice =  product.calculatePrice(this.count);
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void changeCountBy(int newAmount) {
        this.count = newAmount;
    }

    public void addProduct(int addedAmt) {
        this.count += addedAmt;
        this.resetTotalPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProduct that = (CartProduct) o;
        return Objects.equals(cart, that.cart) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cart, product);
    }
}
