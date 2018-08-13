package codesquad.web;

import codesquad.domain.Cart;
import codesquad.domain.Product;
import codesquad.domain.User;
import codesquad.dto.CartProductDTO;
import codesquad.dto.SetCartProductDTO;
import codesquad.security.SessionUtils;
import codesquad.service.CartProductService;
import codesquad.service.ProductService;
import codesquad.support.ApiSuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Slf4j
@RestController
@RequestMapping("/api/cart")
public class ApiCartController {

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private ProductService productService;

    @PostMapping("")
    public ResponseEntity<ApiSuccessResponse> addToCart(@RequestBody CartProductDTO cartProductDTO, HttpSession session) {
        Cart managedCart = cartProductService.initCart(SessionUtils.getUserFromSession(session), SessionUtils.getCartFromSession(session));
        Product product = productService.findById(cartProductDTO.getProductId());
        Cart addedCart = cartProductService.addToCart(cartProductDTO, managedCart, product);
        SessionUtils.setCartInSession(session, addedCart);
        log.debug("after addToCart cart in session {} {}", addedCart, managedCart.hashCode());
        return ResponseEntity.ok(new ApiSuccessResponse(HttpStatus.OK, addedCart, null));
    }

    @PutMapping("")
    public ResponseEntity<ApiSuccessResponse> changeAmount(@RequestBody SetCartProductDTO setCartProductDTO, HttpSession session) {
        Cart cart = SessionUtils.getCartFromSession(session);
        User user = SessionUtils.getUserFromSession(session);
        log.debug("cart in session {} {}", cart, cart.hashCode());
        log.debug("user in session {}", user);
        Cart addedCart = cartProductService.changeCartItem(setCartProductDTO, cart, user);
        SessionUtils.setCartInSession(session, addedCart);
        return ResponseEntity.ok(new ApiSuccessResponse(HttpStatus.OK, addedCart, null));
    }

}
