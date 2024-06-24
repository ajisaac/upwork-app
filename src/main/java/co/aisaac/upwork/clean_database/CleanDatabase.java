package co.aisaac.upwork.clean_database;

import co.aisaac.upwork.model.PostingRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
//@Configuration
public class CleanDatabase {

//    @Autowired
    private PostingRepo repo;

    /**
     * Remove declined jobs every day
     */
//    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    // disable for time being
    void cleanDatabase() {

        log.warn("Deleting all jobs with declined status.");
        repo.deleteAllByStatus("declined");

    }
}
