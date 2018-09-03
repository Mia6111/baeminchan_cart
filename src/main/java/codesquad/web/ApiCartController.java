package codesquad.web;

import codesquad.domain.Cart;
import codesquad.domain.User;
import codesquad.dto.CartProductDTO;
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
public class ApiCartController {

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private ProductService productService;

    @PostMapping("/api/carts")
    public ResponseEntity<ApiSuccessResponse> addToCart(@RequestBody CartProductDTO cartProductDTO, HttpSession session) {
        Cart managedCart = cartProductService.initCart(SessionUtils.getUserFromSession(session), SessionUtils.getCartFromSession(session));

        Cart addedCart = cartProductService.addProductToCart(cartProductDTO, managedCart);

        //todo in posthandle/interceptor
        SessionUtils.setCartInSession(session, addedCart);

        log.debug("after addToCart cart in session {} {}", addedCart, managedCart.hashCode());
        return ResponseEntity.ok(new ApiSuccessResponse(HttpStatus.OK, addedCart, null));
    }

    @PutMapping("/api/carts")
    public ResponseEntity<ApiSuccessResponse> changeAmount(@RequestBody CartProductDTO cartProductDTO, HttpSession session) {
        //todo in prehandle / interceptor -
        Cart cart = SessionUtils.getCartFromSession(session);
        User user = SessionUtils.getUserFromSession(session);

        Cart addedCart = cartProductService.changeCartItem(cartProductDTO, cart, user);
        //todo in posthandle/interceptor
        SessionUtils.setCartInSession(session, addedCart);
        return ResponseEntity.ok(new ApiSuccessResponse(HttpStatus.OK, addedCart, null));
    }

    @DeleteMapping("/api/carts")
    public ResponseEntity<ApiSuccessResponse> deleteCartItem(@RequestBody CartProductDTO cartProductDTO, HttpSession session) {
        //todo in prehandle / interceptor -
        Cart cart = SessionUtils.getCartFromSession(session);
        User user = SessionUtils.getUserFromSession(session);

        Cart addedCart = cartProductService.changeCartItem(cartProductDTO, cart, user);
        //todo in posthandle/interceptor
        SessionUtils.setCartInSession(session, addedCart);
        return ResponseEntity.ok(new ApiSuccessResponse(HttpStatus.OK, addedCart, null));
    }
}
