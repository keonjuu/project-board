package exc.Board.controller.Login;

import exc.Board.controller.Board.BoardController;
import exc.Board.domain.member.Member;
import exc.Board.service.LoginService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@SpringBootTest
@Transactional
@WebMvcTest
public class LoginControllerTest {

    @Autowired LoginService loginService;
    @Autowired BoardController boardController;
    @Autowired MockMvc mockMvc;
    loginForm form = new loginForm();
    @Before
    public void setUp() throws Exception {
//        loginForm form = new loginForm();
        form.setEmail("keonjuu@innotree.com");
        form.setPwd("kkk");
    }

    @Test
    @Transactional
    @Commit
    public void 로그인하기(){

        // 로그인 확인
        Member loginMember = loginService.login(form.getEmail(), form.getPwd());

        //로그인 접속시간 저장
        LocalDateTime lastDateTime = LocalDateTime.now();
        loginMember.setLastDatetime(lastDateTime);
   /*     System.out.println("loginMember = " + loginMember);*/

        // db 값과 객체 loginMember 값이 일치하는지 확인
        Assertions.assertThat(loginMember.getLastDatetime()).isEqualTo(lastDateTime);
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