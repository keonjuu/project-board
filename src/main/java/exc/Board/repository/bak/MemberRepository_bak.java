package exc.Board.repository.bak;

import exc.Board.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository_bak {

    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long saveId) {
        return em.find(Member.class,saveId);
    }

    public void delete(Long userId) {
        Member member = em.find(Member.class, userId);
        em.remove(member);
    }
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
/*                .setFirstResult(0)
                .setMaxResults(3)*/
                .getResultList();
    }

    public Optional<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.userName = :userName", Member.class)
                .setParameter("userName", name)
                .getResultStream().findAny();
    }

    public Optional<Member> findByEmail(String email){
        return em.createQuery("select m from Member m where m.email = :userEmail", Member.class)
                .setParameter("userEmail", email)
                .getResultStream().findAny();
    }


    public List<Member> findAllByName(String name) {
        return em.createQuery("select m from Member m where m.userName = :userName", Member.class)
                .setParameter("userName", name)
                .getResultList();

    }

}
