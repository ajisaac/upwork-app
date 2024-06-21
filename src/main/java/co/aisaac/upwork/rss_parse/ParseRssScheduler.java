package co.aisaac.upwork.rss_parse;

import co.aisaac.upwork.automated_filters.CountryFilter;
import co.aisaac.upwork.model.Posting;
import co.aisaac.upwork.model.PostingRepo;
import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Slf4j
@Configuration
public class ParseRssScheduler {

    @Autowired
    PostingRepo postingRepo;

    CountryFilter countryFilter = new CountryFilter();

    @Scheduled(fixedDelay = 1 * 60 * 1000)
    public void run() throws IOException {
        String url = "https://www.upwork.com/ab/feed/jobs/rss" +
                "?category2_uid=531770282580668418" +
                "&contractor_tier=2%2C3" +
                "&sort=recency" +
                "&t=0" +
                "&api_params=1" +
                "&securityToken=f2b1425b06ba5e895d96d6a621a0551c9cb3a8bee728999cb662cb574696abcffa77f426df80aff253a7028422fdcda0c91ff972aede682c39c4300e3bed2801" +
                "&userUid=1299200877346168832" +
                "&orgUid=1299200877350363137";

        RssReader rssReader = new RssReader();

        for (Item item : rssReader.read(url).toList()) {

            Posting posting = ParseRssItem.parse(item);
            if (posting == null) {
                log.warn("Unable to parse RSS feed item.");
                continue;
            }

            // filter country
            if (countryFilter.contains(posting.country)) {
                log.warn("Not saving job from country: {}", posting.country);
                continue;
            }

            if (postingRepo.existsByGuid(posting.guid) == false) {
                log.info("Saving job {}", posting.url);
                System.out.println(posting);
                System.out.println("\n");
                postingRepo.save(posting);
            } else {
                log.info("Job already exists {}", posting.url);
            }

        }

        // tomorrow
        // store postings in database
        // send emails or do notification when java postings show up
    }


}
