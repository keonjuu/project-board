package exc.Board.service;

import exc.Board.domain.member.Member;
import exc.Board.domain.member.MemberStatus;
import exc.Board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    public void JoinYn(Member member){
        memberRepository.findByEmail(member.getEmail());
        memberRepository.save(member);
    }
}
