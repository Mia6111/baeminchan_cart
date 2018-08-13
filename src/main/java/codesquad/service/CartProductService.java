package codesquad.service;

import codesquad.domain.*;
import codesquad.dto.CartProductDTO;
import codesquad.dto.SetCartProductDTO;
import codesquad.exception.NotAuthorizedException;
import codesquad.exception.ResourceNotFoundException;
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
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    PriceCalcultor priceCalcultor;

    public CartProductService(PriceCalcultor priceCalcultor) {
        this.priceCalcultor = priceCalcultor;
    }

    public CartProduct initCartProduct(CartProductDTO cartProductDTO, Cart cart, Product product) {
        return cartProductDTO.create(cart, product, priceCalcultor);
    }
    @Transactional
    public Cart addToCart(CartProductDTO dto, Cart cart, Product product) {
        CartProduct cartProduct = initCartProduct(dto, cart, product);
        cartProduct = cartProductRepository.saveAndFlush(cartProduct);
        return cartProduct.getCart();
    }

    public Cart initCart(User user, Cart cartFromSession){
        Cart managedCart = createCartIfEmpty(cartFromSession);
        managedCart = saveUserIfLoggedIn(user, managedCart);
        return managedCart;
    }

    public Cart createCartIfEmpty(Cart cartFromSession) {
        if (cartFromSession.isEmptyCart()) {
            cartFromSession = cartRepository.saveAndFlush(new Cart());
        }
        return cartFromSession;
    }

    private Cart saveUserIfLoggedIn(User loginUser, Cart managedCart) {
        if (!loginUser.isGuestUser()) {
            managedCart.setUser(loginUser);
            managedCart = cartRepository.saveAndFlush(managedCart);
        }
        log.debug("user added {}", managedCart);
        return managedCart;
    }

//    public Cart saveUserOfCart(Cart cartFromSession, User user){
//        Optional<Cart> originalCart = getCartByUser(user);
//        if(originalCart.isPresent())
//            return originalCart.get();
//
//        Cart managedCart = createCartIfEmpty(cartFromSession);
//        return saveUserIfLoggedIn(user, managedCart);
//
//        if(cartFromSession != null && originalCart.isPresent()){
//        }
//    }
    public Optional<Cart> getCartByUser(User user) {
        if (!user.isGuestUser())
            return findCartByUserId(user.getId());
        return Optional.empty();
    }

    private Optional<Cart> findCartByUserId(long id) {
        return cartRepository.findByUserId(id);
    }

    @Transactional
    public Cart changeCartItem(SetCartProductDTO setCartProductDTO, Cart cart, User user) {
        //todo refactor
        log.debug("cart ", cart);
        if (!user.isGuestUser() && !cart.isOwner(user)) {
            throw new NotAuthorizedException();
        }
        log.debug("setCartProductDTO {}", setCartProductDTO);
        // update가 아닌 insert 문으로 실행된다. 왜일까?
        CartProduct cartProduct = cart.getCartProducts().stream().filter(x -> x.getId() == setCartProductDTO.getCartId()).findFirst().orElseThrow(ResourceNotFoundException::new);
//        cartProduct.setCount(setCartProductDTO.getCount());
        Cart changedCart = cartRepository.save(cart);
        log.debug("cartProduct Exists {}", cartProduct);

        log.debug("changedCart {}", cartProduct.getCart());
        return changedCart; //cartProduct.getCart();
    }
}
