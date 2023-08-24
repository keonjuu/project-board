package com.Board.controller.Login;

import com.Board.Login.LoginForm;
import com.Board.Login.LoginService;
import com.Board.Member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
public class LoginControllerTest {

    @Autowired
    private LoginService loginService;
    private LoginForm form;

    @Before
    public void setUp() {
       form = LoginForm.builder()
                .email("hello102@innotree.com")
                .pwd("hello102")
                .build();
    }
    @Test
    @DisplayName("로그인")
    public void login(){

        log.info("form = {}", form);

        // 로그인 확인
        Member loginMember = loginService.login(form.getEmail(), form.getPwd());
        log.info("loginMember = {}" , loginMember);

//        // db 값과 객체 loginMember 값이 일치하는지 확인
        Assertions.assertThat(loginMember.getEmail()).isEqualTo(form.getEmail());

    }


/*    @Test
    public void 인터셉터테스트() throws Exception{
        mockMvc.perform(post("login?redirectURL=/Board/2/view"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("redirectURL").value("/Board/2/view"));


*//*        mockMvc.perform(post("/events").param("name", "sungwon").param("limit", "20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("name").value("sungwon"));*//*

    }*/


}