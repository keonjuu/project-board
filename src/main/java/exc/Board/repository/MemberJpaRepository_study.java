package exc.Board.repository;

import exc.Board.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

//@Repository // 어노테이션 생략가능!
public interface MemberJpaRepository_study extends JpaRepository<Member, Long> {
//    List<Member> findByUsername();
    List<Member> findByUserName(@Param("username") String username);

    // 리포지토리 메소드 쿼리 정의 - findUser
    @Query("select m from Member m where m.userName = :username")
    List<Member> findUser(@Param("username") String username);

    // 특정 컬럼 값
    @Query("select m.userName from Member m")
    List<String> findUserNameList();

    // DTO 조회하기 - MemberForm
/*
    @Query("select new exc.Board.controller.Member.MemberForm(m.userName) from Member m ")
    List<MemberForm> findMemberForm();
*/

    // 파라미터 바인딩
    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    // 반환타임
/*    List<Member> findByUserNames(String username); // 컬랙션

    Member findMemberByUserName(String username);  // 단건
    Optional<Member> findOpticalMemberByUserName(String username); // 단건 optical*/

//    Page<Member> findByUserName(String userName, Pageable pageable);
    Slice<Member> findByUserName(String userName, Pageable pageable);


}
