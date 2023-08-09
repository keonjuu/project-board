package exc.Board.service;

import exc.Board.domain.board.Board;
import exc.Board.domain.member.Member;
import exc.Board.repository.BoardRepository;
import exc.Board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    // 회원가입
    @Transactional
    public Long join(Member member) {

        //isDuplicatedEmail(member); // 이메일 중복 체크 추가하기
        memberRepository.save(member);
        return member.getId(); //
    }
/*    @Transactional
    public void isValidEmail(){
    }
*/
//    @Transactional
    public boolean validDuplicatedEmail(String email) {
        System.out.println("validDuplicatedEmail.email = " + email);
        if(memberRepository.findByEmail(email).isPresent()){
            return true;
        }else{
            return false;
        }
    }


    @Transactional
    public void isDuplicatedEmail(Member member) {
        System.out.println("member.getEmail() = " + member.getEmail());
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 사용중인 이메일입니다.");  // ifPresent 는 Optional이기 때문 가능.
                });
    }

    @Transactional
    public void delete(Long userId) {
        memberRepository.delete(userId);
    }

    /*
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    */
    public Page<Member> findAll(Pageable pageable){

        int pageNumber = pageable.getPageNumber()==0 ? 0 : pageable.getPageNumber()-1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.ASC, "id"));

        Page<Member> pageMember = memberRepository.findAll(pageable);

        return pageMember;
//        Slice<Member> slice = memberRepository.findAll(pageRequest);
/*
        List<Member> memberList = pageMember.getContent();
        System.out.println("memberList size = " + memberList.size());
        for (Member member : memberList) {
            System.out.println("member = " + member);
        }
        return memberList;*/
    }


    public Optional<Member> findOne(Member member) {
        return Optional.ofNullable(memberRepository.find(member.getId()));
    }

    public Member findById(Long id) {
        return memberRepository.find(id);
    }


    // user별 리스트 조회
    public Page<Board> findAllWithBoard(Long id, Pageable pageable){

        int pageNumber = pageable.getPageNumber()==0 ? 0 : pageable.getPageNumber()-1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "modTime"));

        return boardRepository.findAllByUserNo(id, pageable);

    }
}