package codesquad.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.OrderBy;
import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    @OrderBy("created_at")
    List<CartProduct> findByCartId(Long cartId);
    Optional<CartProduct> findByCartAndId(Cart cart, Long id);
    Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);
}
