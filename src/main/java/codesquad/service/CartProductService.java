package codesquad.service;

import codesquad.domain.*;
import codesquad.dto.CartProductDTO;
import codesquad.exception.NotAuthorizedException;
import codesquad.exception.ResourceNotFoundException;
import codesquad.security.SessionUtils;
import codesquad.support.PriceCalcultor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class CartProductService {
    @Autowired
    private ProductService productService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    PriceCalcultor priceCalcultor;

    public CartProductService(PriceCalcultor priceCalcultor) {
        this.priceCalcultor = priceCalcultor;
    }

    @Transactional
    public Cart addProductToCart(CartProductDTO dto, Cart sessionedCart) {
        Product product = productService.findById(dto.getProductId());

        CartProduct originalCartProduct = cartProductRepository.findByCartAndProduct(sessionedCart, product)
                .orElseGet(() -> convertToCartProduct(dto, sessionedCart));

        sessionedCart.addProduct(originalCartProduct);
        originalCartProduct = cartProductRepository.save(originalCartProduct);
        log.debug("addedProductToCart ### CartProduct {} ### Cart {}", originalCartProduct, originalCartProduct.getCart());
        log.debug("addedProductToCart SessionedCart {}", sessionedCart);
        return sessionedCart;
    }


    private CartProduct convertToCartProduct(CartProductDTO dto, Cart cart) {
        return CartProduct.builder()
                .cart(cart)
                .product(productService.findById(dto.getProductId()))
                .count(dto.getCount()).build();
    }

    public Cart initCart(User user, Cart cartFromSession){
        Cart managedCart = createCartIfEmpty(cartFromSession);
        managedCart = assignUserIfLoggedIn(user, managedCart);
        managedCart = cartRepository.save(managedCart);
        log.debug("createCartIfEmpty initCart {} ", managedCart);

        return managedCart;
    }
    //todo > 다시 체크
    public Cart createCartIfEmpty(Cart cartFromSession) {
        if (cartFromSession.isEmptyCart()) {
            cartFromSession = new Cart(null, 0);
        }
        return cartFromSession;
    }


    private Cart assignUserIfLoggedIn(User loginUser, Cart managedCart) {
        if (!loginUser.isGuestUser()) {
            managedCart.assignOwner(loginUser);
        }
        log.debug("user added {}", managedCart);
        return managedCart;
    }
    private Optional<Cart> findCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Transactional
    public Cart changeCartItem(CartProductDTO cartProductDTO, Cart cart, User user) {
        //todo refactor
        log.debug("cart ", cart);
        if (!user.isGuestUser() && !cart.isOwner(user)) {
            throw new NotAuthorizedException();
        }
        CartProduct cartProduct = getCartProduct(cart, cartProductDTO.getCartProductId());
        cartProduct.changeCountBy(cartProductDTO.getCount());
        log.debug("cartProduct Updated {}", cartProduct);

        log.debug("changedCart {}", cartProduct.getCart());
        return cartProduct.getCart();
    }

    private CartProduct getCartProduct(Cart cart, long cartProductId) {
        return cartProductRepository.findByCartAndId(cart, cartProductId)
                .orElseThrow(ResourceNotFoundException::new);

    }

    public Cart assignUserToCart(User loginUser, Cart cartFromSession) {
        Cart managedCart = new Cart(loginUser, 0);
        if(!cartFromSession.isEmptyCart()){
            cartFromSession.assignOwner(loginUser);
            managedCart = new Cart(cartFromSession);
        }
        cartRepository.save(managedCart);
        return managedCart;
    }

    public Cart getCartofLoginUser(User loginUser, Cart cartFromSession) {
        return findCartByUser(loginUser)
                .orElseGet(() -> assignUserToCart(loginUser, cartFromSession ));
    }
}
