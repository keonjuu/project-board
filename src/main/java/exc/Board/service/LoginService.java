package exc.Board.service;

import exc.Board.domain.member.Member;
import exc.Board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final MemberRepository memberRepository;

    // 로그인
    public Member login(String email, String password){
        /*Optional<Member> findMember = memberRepository.findByEmail(email);
        Member member = findMember.get();
        if(member.getPassword() == password){
             return member;
        }
        return null;*/
        return memberRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    @Transactional
    public void save(Member member){
        memberRepository.save(member);
    }
}
