package exc.Board.controller.admin;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberStatus;
import exc.Board.repository.MemberRepository;
import exc.Board.service.AdminService;
import exc.Board.service.MemberService;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // JUnit 실행시 Spring과 함께 실행
@SpringBootTest
@Transactional
public class adminControllerTest {
    @Autowired AdminService adminService;
    @Autowired MemberRepository memberRepository;

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
        System.out.println("findMember = " + findMember);*//*
    }*/
    @Test
    @Transactional
//    @Commit
    public void 승인하기(){
        findMember = memberRepository.find(1L);
        findMember.setStatus(MemberStatus.ADMISSION);

        System.out.println("setStatus findMember = " + findMember);

        adminService.JoinYn(findMember);

        // 다시 꺼낸 값이랑 비교
        MemberStatus status = memberRepository.find(1L).getStatus();
        Assertions.assertThat(status).isEqualTo(findMember.getStatus());
    }
}