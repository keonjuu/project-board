package com.Board.controller.Admin;

import com.Board.Member.entity.Member;
import com.Board.Member.entity.MemberStatus;
import com.Board.Member.MemberRepository;
import com.Board.Admin.AdminService;
import com.Board.Member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class) // JUnit 실행시 Spring과 함께 실행
@SpringBootTest
@Transactional
public class AdminControllerTest {
    @Autowired private AdminService adminService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    Member adminMember;
    Member findMember;

/*    @BeforeEach
    public void 회원찾기(){
        //given
        Long adminId = 1001L;
        Long findId = 1L;
        //when
        adminMember = memberRepository.find(adminId);
        findMember = memberRepository.find(findId);
*//*        //then
        log.info("findMember = " + findMember);*//*
    }*/
    @Test
    @DisplayName("승인하기")
//    @Commit
    public void joinYn(){

        findMember = memberRepository.find(1L);
        findMember.toBuilder().status(MemberStatus.REJECT);

        log.info("setStatus findMember = {}" , findMember);
        adminService.joinYn(findMember);

        // 다시 꺼낸 값이랑 비교
        MemberStatus status = memberRepository.find(1L).getStatus();
        Assertions.assertThat(status).isEqualTo(findMember.getStatus());
    }
}