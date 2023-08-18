package exc.Board.service;

import exc.Board.domain.member.Member;
import exc.Board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final MemberRepository memberRepository;

    @Transactional
    public void joinYn(Member member){
        //memberRepository.findByEmail(member.getEmail());
        memberRepository.save(member);
    }
}
