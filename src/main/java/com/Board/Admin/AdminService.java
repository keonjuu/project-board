package com.Board.Admin;

import com.Board.Member.entity.Member;
import com.Board.Member.MemberRepository;
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
