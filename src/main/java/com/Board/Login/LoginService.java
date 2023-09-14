package com.Board.Login;

import com.Board.Member.entity.Member;

public interface LoginService {
    Member login(String email, String password);
    void save(Member member);

}
