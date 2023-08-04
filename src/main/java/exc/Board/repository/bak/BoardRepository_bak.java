package exc.Board.repository.bak;

import exc.Board.domain.Board;
import exc.Board.domain.BoardCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository_bak {

    private final EntityManager em;

    //1. save
    public void save(Board board){
        em.persist(board);
    }

    //2. findAll - paging처리 필요
    public List<Board> findAll(){
        return em.createQuery("select b from Board b where b.delYn='N'", Board.class)
//                .setFirstResult(0)
//                .setMaxResults(10)
                .getResultList();
    }

    //3. find 게시글 번호 상세조회
    public Board findOne(Long boardNo){
        Board board = em.createQuery("select b from Board b where b.boardNo=:boardNo", Board.class)
                .setParameter("boardNo", boardNo)
                .getSingleResult();
        return board;

    }

    // 4.  find 카테고리별 조회
    public List<Board> findCategory(BoardCategory category){
        return em.createQuery("select b from Board b where b.boardCategory=:boardCategory", Board.class)
                .setParameter("boardCategory", category)
                .getResultList();

    }


    /*    public List<Board> findAllByUserName(String name){
            return em.createQuery("select b from Board b join Member m where m.userName =:userName", Board.class)
                    .setParameter("userName", name)
                    .getResultList();
        }*/


}
