package codesquad.dto;

import codesquad.domain.Cart;
import codesquad.domain.CartProduct;
import codesquad.domain.Product;
import codesquad.domain.User;
import codesquad.support.PriceCalcultor;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@NoArgsConstructor @Getter @Setter
public class CartProductDTO {
   // private Product product;
   // private Cart cart;
    @NotNull
    @Min(1)
    private int count = 1;
    @NotNull
    private long productId;

    private long cartProductId;
//    private Long totalPrice;

    @Builder
    public CartProductDTO(int count, long productId, long cartProductId) {
        this.count = count;
        this.productId = productId;
        this.cartProductId = cartProductId;
    }

}