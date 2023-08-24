package com.Board.Member;

import com.Board.Member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //@EntityGraph(attributePaths = {"boards"}, type= EntityGraph.EntityGraphType.LOAD)

    /*// lazy 로딩된 일대 다 엔티티 불러오기 (fetch join vs join )
    @Query("select m from Member m join m.boards b where m.id = :id")
    Page<Member> findALLWithBoardList(@Param("id") Long userId, Pageable pageable);*/


//    Long save(Member member);

    @Query("select m from Member m where m.id= :id")
    Member find(@Param("id") Long saveId);

    @Modifying
    @Query("delete from Member m where m.id= :id")
    void delete(@Param("id") Long userId);

//    List<Member> findAll();
    Page<Member> findAll(Pageable pageable);
//    Slice<Member> findByUserName(String userName, Pageable pageable);

    @Query("select m from Member m where m.userName= :userName")
    Optional<Member> findByName(@Param("userName") String name);

    Optional<Member> findByEmail(String email);


    @Query("select m from Member m where m.userName = :userName")
    List<Member> findAllByName(@Param("userName") String name);

}
