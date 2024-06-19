package co.aisaac.upwork;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepo extends CrudRepository<Posting, Long> {

}
