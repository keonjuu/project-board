package exc.Board.repository;

import exc.Board.domain.Board;
import exc.Board.domain.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
//@RequiredArgsConstructor
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


}
