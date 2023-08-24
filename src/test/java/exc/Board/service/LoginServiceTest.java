package exc.Board.service;

import exc.Board.domain.member.Member;
import exc.Board.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class) // JUnit 실행시 Spring과 함께 실행
@SpringBootTest
@Transactional(readOnly = true)
public class LoginServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("패스워드 암호화 로그인 테스트")
    public void login(){
        //given
        String email = "hello102@innotree.com";
        String password = "hello102";
        //when
        Member member = memberRepository.findByEmail(email)
                .filter(m -> passwordEncoder.matches(password, m.getPassword()))
                .orElse(null);
        log.info("member = {}", member);

        //then
        Assertions.assertTrue(passwordEncoder.matches(password, member.getPassword()));
    }
}