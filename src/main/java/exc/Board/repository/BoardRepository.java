package exc.Board.repository;

import exc.Board.domain.board.Board;
import exc.Board.domain.board.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

//    private final EntityManager em;

    //1. save
/*
    public void save(Board board){
        em.persist(board);
    }
*/

    //2. findAll - paging처리 필요
    @Query("select b from Board b where b.delYn='N'")
    Page<Board> findAll(Pageable pageable);
//    List<Board> findAll();



    //3. find 게시글 번호 상세조회
    Board findByBoardNo(Long boardNo);

    // 4.  find 카테고리별 조회 -> 페이징 처리 필요
    @Query("select b from Board b where b.delYn='N' and b.boardCategory=:boardCategory")
    Page<Board> findCategory(@Param("boardCategory") BoardCategory category, Pageable pageable);


    // 5. 검색 기능
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);
    Page<Board> findByContentContaining(String keyword, Pageable pageable);
    @Query("select b from Board b where b.delYn='N' and b.member.email like %:keyword%")
    Page<Board> findByRegIdContaining(@Param("keyword") String keyword, Pageable pageable);


    Page<Board> findByBoardCategoryAndDelYnEqualsAndTitleContaining(BoardCategory category,String delYn,String keyword, Pageable pageable);
    Page<Board> findByBoardCategoryAndDelYnEqualsAndContentContaining(BoardCategory category,String delYn, String keyword, Pageable pageable);

    @Query("select b from Board b where b.boardCategory=:boardCategory and b.delYn='N' and (b.title like %:keyword% or b.content like %:keyword%)")
    Page<Board> findTitleOrContentContaining(@Param("boardCategory") BoardCategory category, @Param("keyword") String keyword, Pageable pageable);

    // 6. 사용자별 게시글 (fetch join vs join )
    @Query("select b from Board b inner join Member m on b.member.email = m.email where m.id =:id")
    Page<Board> findAllByUserNo(@Param("id") Long id, Pageable pageable);


}
