package com.Board.Login;

import com.Board.Member.entity.Member;
import com.Board.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    @Override
    public Member login(String email, String password){

        return memberRepository.findByEmail(email)
                .filter(m -> passwordEncoder.matches(password,m.getPassword()))
                .orElse(null);
//        log.info("member = {}" , member);
    }

    @Override
    @Transactional
    public void save(Member member){

        memberRepository.save(member);
    }
}
