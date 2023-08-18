package exc.Board.service;

import exc.Board.domain.member.Member;
import exc.Board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LoginService {
    private final MemberRepository memberRepository;

    // 로그인
    public Member login(String email, String password){
        return memberRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
//        log.info("member = {}" , member);

    }

    @Transactional
    public void save(Member member){

        memberRepository.save(member);
    }
}
