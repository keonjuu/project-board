package com.Board.service;

import com.Board.Member.MemberService;
import com.Board.Member.dto.MemberForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
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
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    MemberForm form;

/*    @Before
    public void init(){
        form = MemberForm.builder()
                .name("hello103")
                .email("hello103@innotree.com")
                .pwd(passwordEncoder.encode("hello103"))  // 패스워드 인코더
                .build();
    }*/

    @Test
    @DisplayName("회원가입")
    public void join(){
        //given (어떤 데이터를 기반으로 하는지)
        form = MemberForm.builder()
                .name("hello103")
                .email("hello103@innotree.com")
                .pwd(passwordEncoder.encode("hello103"))  // 패스워드 인코더
                .build();
//        member = form.toEntity(form);

        //when (가입 서비스 검증할 떄)
        Long saveId = memberService.join(form);
        log.info("saveId = {}",  saveId);
        //then (저장한 값이 리포지토리에 있는게 맞아? )
/*        Member findMember = memberService.findOne(form).orElse(null);
        log.info("form = {} , findMember = {}" , form , findMember);*/

    }

    @Test
    @DisplayName("이메일체크")
    public void checkEmail(){
        String email = "kkk@naver.com";
        boolean result = memberService.validDuplicatedEmail(email);
        log.info("중복? = {}" , result);
    }



}