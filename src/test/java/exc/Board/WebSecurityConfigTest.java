package exc.Board;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BoardApplication.class)
@SpringBootTest
public class WebSecurityConfigTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("패스워드 암호화 테스트")
    public void passwordEncode() {
        // given
        String rawPassword = "12345678";

        // when
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // then
        assertAll(
                () -> assertNotEquals(rawPassword, encodedPassword),
                () -> assertTrue(passwordEncoder.matches(rawPassword, encodedPassword))
        );
    }
}
