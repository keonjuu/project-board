package com.Board.service;

import com.Board.Member.dto.MemberForm;
import com.Board.Member.MemberService;
import com.Board.Member.entity.Member;
import com.Board.Member.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class) // JUnit 실행시 Spring과 함께 실행
@SpringBootTest
@Transactional
public class MemberServiceTest {
    private static Logger logger = LoggerFactory.getLogger(MemberServiceTest.class);
    @Autowired
    MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;

    MemberForm form;
    Member member;
/*    @BeforeEach
    void init(){
        form = MemberForm.builder()
                .name("hello100")
                .email("hello100@innotree.com")
                .pwd("hello100")
                .build();
        member = Member.toEntity(form);
    }*/

    @Test
    @Commit
    public void 회원가입(){
        //given (어떤 데이터를 기반으로 하는지)
        form = MemberForm.builder()
                .name("hello102")
                .email("hello102@innotree.com")
                .pwd(passwordEncoder.encode("hello102"))  // 패스워드 인코더
                .build();
        member = form.toEntity(form);
        logger.info("form = {} , member = {}" , form, member);

        //when (가입 서비스 검증할 떄)
        Long saveId = memberService.join(form);

        /*System.out.println("saveId = " + saveId);

        //then (저장한 값이 리포지토리에 있는게 맞아? )
        Member findMember = memberService.findOne(member).orElse(null);
        assertThat(member.getUserName()).isEqualTo(findMember.getUserName()); // 이름 검증*/

    }

    @Test
    public void 이메일체크(){
        String email = "kkk@naver.com";
        boolean result = memberService.validDuplicatedEmail(email);
        System.out.println("중복? = " + result);
    }



}