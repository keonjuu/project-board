package com.Board.controller.Login;

import com.Board.Board.BoardController;
import com.Board.Login.LoginForm;
import com.Board.Login.LoginService;
import com.Board.Member.entity.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
//@SpringBootTest
@Transactional
@WebMvcTest

public class LoginControllerTest {
    private static Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);

    @Autowired LoginService loginService;
    @Autowired BoardController boardController;
    @Autowired MockMvc mockMvc;

    private LoginForm form;
    @Before
    public void setUp() throws Exception {
        form.builder().email("keonjuu@innotree.com").pwd("kkk").build();
//        loginForm form = new loginForm();
//        form.setEmail("keonjuu@innotree.com");
//        form.setPwd("kkk");
    }

    @Test
    @Transactional
//    @Commit
    public void 로그인하기(){

        logger.info("form = {}", form);
//        form.builder().email("keonjuu@innotree.com").pwd("kkk").build();
        // 로그인 확인
//        Member loginMember = loginService.login(form.getEmail(), form.getPwd());
        Member loginMember = loginService.login(form.getEmail(), form.getPwd());

        logger.info("loginMember = {}" , loginMember);
        //로그인 접속시간 저장
//        loginMember.setLastDatetime(lastDateTime);
   /*     log.info("loginMember = {}", loginMember);*/

//        // db 값과 객체 loginMember 값이 일치하는지 확인
//        Assertions.assertThat(loginMember.getLastDatetime()).isEqualTo(lastDateTime);
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