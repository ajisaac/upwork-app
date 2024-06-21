package co.aisaac.upwork.rss_parse;

import co.aisaac.upwork.automated_filters.CountryFilter;
import co.aisaac.upwork.model.Posting;
import co.aisaac.upwork.model.PostingRepo;
import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Slf4j
@Configuration
public class ParseRssScheduler {

    @Autowired
    PostingRepo postingRepo;

    @Value("${application.rss.security_token}")
    String securityToken;
    @Value("${application.rss.user_uid}")
    String userUid;
    @Value("${application.rss.org_uid}")
    String orgUid;

    CountryFilter countryFilter = new CountryFilter();

    // every minute
    @Scheduled(fixedDelay = 60 * 1000)
    public void run() throws IOException {
        String url = "https://www.upwork.com/ab/feed/jobs/rss" +
                "?category2_uid=531770282580668418" +
                "&contractor_tier=2%2C3" +
                "&sort=recency" +
                "&t=0" +
                "&api_params=1" +
                "&securityToken=" + securityToken +
                "&userUid=" + userUid +
                "&orgUid=" + orgUid;

        RssReader rssReader = new RssReader();

        for (Item item : rssReader.read(url).toList()) {

            // parse RSS item
            Posting posting = ParseRssItem.parse(item);
            if (posting == null) {
                log.warn("Unable to parse RSS feed item.");
                continue;
            }

            // filter country
            if (countryFilter.contains(posting.country) == false) {
                log.warn("Not saving job from {}.", posting.country);
                continue;
            }

            // hourly only
            if (posting.hourlyRange == null || posting.hourlyRange.isBlank()) {
                log.warn("Job skipped, only parsing hourly jobs.");
                continue;
            }

            // filter out lower paying jobs
            if (hasLowBudget(posting, 40)) {
                log.warn("Job skipped, budget was only {}.", posting.hourlyRange);
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
    }

    private boolean hasLowBudget(Posting posting, int lowestDollarAmount) {

        // parse hourly rating
        String hourly = posting.getHourlyRange();
        if (hourly == null || hourly.isBlank()) {
            return true;
        }

        // ex. $4.00-$5.00
        String[] split = hourly.split("-");
        if (split.length > 2) {
            // shouldn't ever happen
            log.warn("The hourly range for this entry is odd: {}.", hourly);
        }

        int high = 0;
        try {
            // grab last number remove dollar sign
            if (split.length == 1) {
                high = (int) Float.parseFloat(split[0].substring(1));
            } else if (split.length == 2) {
                high = (int) Float.parseFloat(split[1].substring(1));
            }
        } catch (NumberFormatException nfe) {
            log.error("Unable to parse hourly range {}.", hourly);
            return false; // probably not that common, we will accept these for further investigation
        }
        return high < lowestDollarAmount;

    }
}
