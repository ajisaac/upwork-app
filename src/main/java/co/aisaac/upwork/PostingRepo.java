package co.aisaac.upwork;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepo extends CrudRepository<Posting, Long> {


    List<Posting> findAll();

    boolean existsByGuid(String guid);
}
