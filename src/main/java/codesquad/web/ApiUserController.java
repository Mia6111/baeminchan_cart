package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.LoginDTO;
import codesquad.dto.UserDTO;
import codesquad.security.SessionUtils;
import codesquad.service.CartService;
import codesquad.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class ApiUserController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "cartService")
    private CartService cartService;
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public void signup(@Valid @RequestBody UserDTO userDTO) {
        userService.save(userDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(HttpSession session, @RequestBody LoginDTO loginDTO) {
        User loginUser = userService.login(loginDTO);
        SessionUtils.setUserInSession(session, loginUser);
        //todo 세션에 카테고리ID가 있는경우, 카테고리 테이블 업데이트
        cartService.saveUser(loginUser, SessionUtils.getCartFromSession(session));
    }
}
