package com.Board.Member;

import com.Board.Board.entity.Board;
import com.Board.Member.dto.MemberForm;
import com.Board.Member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberService {
    Long join(MemberForm memberForm);
    boolean validDuplicatedEmail(String email);
    void isDuplicatedEmail(Member member);
    void delete(Long userId);
    Page<Member> findAll(Pageable pageable);
    Optional<Member> findOne(MemberForm memberform);
    Member findById(Long id);
    Page<Board> findAllWithBoard(Long id, Pageable pageable);
}
