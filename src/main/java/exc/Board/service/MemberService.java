package exc.Board.service;

import exc.Board.domain.member.Member;
import exc.Board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Long join(Member member){

        isDuplicatedEmail(member); // 이메일 중복 체크 추가하기
        memberRepository.save(member);
        return member.getId(); //
    }
/*    @Transactional
    public void isValidEmail(){
    }
*/

    @Transactional
    public void isDuplicatedEmail(Member member){
        System.out.println("member.getEmail() = " + member.getEmail());
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 사용중인 이메일입니다.");  // ifPresent 는 Optional이기 때문 가능.
                });
    }

    @Transactional
    public void delete(Long userId){
        memberRepository.delete(userId);
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Member member){
        return Optional.ofNullable(memberRepository.find(member.getId()));
    }
}
