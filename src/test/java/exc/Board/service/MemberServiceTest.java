package exc.Board.service;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberType;
import exc.Board.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class) // JUnit 실행시 Spring과 함께 실행
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Commit
    public void 회원가입(){
        //given (어떤 데이터를 기반으로 하는지)
        Member member = new Member();
        member.setUserName("hello100");
        member.setEmail("hello100@innotree.com");
        member.setMemberType(MemberType.USER);
        member.setPassword("hoooo");

        System.out.println("member = " + member);

        //when (가입 서비스 검증할 떄)
        Long saveId = memberService.join(member);

        //then (저장한 값이 리포지토리에 있는게 맞아? )
        Member findMember = memberService.findOne(member).get();
        assertThat(member.getUserName()).isEqualTo(findMember.getUserName()); // 이름 검증

    }
}