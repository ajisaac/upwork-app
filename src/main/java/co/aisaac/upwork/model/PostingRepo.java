package co.aisaac.upwork.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepo extends CrudRepository<Posting, Long> {


    List<Posting> findAll();

    @Query("SELECT p FROM postings p ORDER BY p.pubDate DESC")
    List<Posting> findAllOrderByPostDate();

    boolean existsByGuid(String guid);
}
