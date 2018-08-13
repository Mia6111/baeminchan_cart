package codesquad.web;

import codesquad.support.test.AcceptanceTest;
import codesquad.support.test.HtmlFormDataBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class RedirectControllerTest extends AcceptanceTest {
@Test
    public void test(){
        assertThat("A").isEqualTo("A");
    }
}