package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.LoginDTO;
import codesquad.dto.UserDTO;
import codesquad.exception.ValidationError;
import codesquad.exception.ValidationErrorResponse;
import codesquad.support.test.AcceptanceTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserControllerAcceptanceTest extends AcceptanceTest {



}