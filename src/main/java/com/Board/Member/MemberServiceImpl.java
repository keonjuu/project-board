package com.Board.Member;


import com.Board.Board.repository.BoardRepository;
import com.Board.Board.entity.Board;
import com.Board.Member.dto.MemberForm;
import com.Board.Member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Override
    @Transactional
    public Long join(MemberForm memberForm) {
        //isDuplicatedEmail(member); // 이메일 중복 체크 추가하기

        // passwordEncoder
        memberForm.setPwd(passwordEncoder.encode(memberForm.getPwd()));

        // dto -> member entity 변환
        Member member = MemberForm.toEntity(memberForm);

        memberRepository.save(member);
        return member.getId(); //
    }

//    @Transactional
    @Override
    public boolean validDuplicatedEmail(String email) {
        log.info("validDuplicatedEmail.email = {}" , email);
        if(memberRepository.findByEmail(email).isPresent()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional
    public void isDuplicatedEmail(Member member) {
        log.info("member.getEmail() = {}" , member.getEmail());
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 사용중인 이메일입니다.");  // ifPresent 는 Optional이기 때문 가능.
                });
    }
    @Override
    @Transactional
    public void delete(Long userId) {
        memberRepository.delete(userId);
    }


    @Override
    public Page<Member> findAll(Pageable pageable){

        int pageNumber = pageable.getPageNumber()==0 ? 0 : pageable.getPageNumber()-1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.ASC, "id"));

        return memberRepository.findAll(pageable);
    }

    @Override
    public Optional<Member> findOne(MemberForm memberform) {
        // dto -> entity 변환
        Member member = MemberForm.toEntity(memberform);

        return Optional.ofNullable(memberRepository.find(member.getId()));
    }
    @Override
    public Member findById(Long id) {
        return memberRepository.find(id);
    }


    // user별 리스트 조회
    @Override
    public Page<Board> findAllWithBoard(Long id, Pageable pageable){

        int pageNumber = pageable.getPageNumber()==0 ? 0 : pageable.getPageNumber()-1;
        pageable = PageRequest.of(pageNumber, 5, Sort.by(Sort.Direction.DESC, "modTime"));

        return boardRepository.findAllByUserNo(id, pageable);

    }
}