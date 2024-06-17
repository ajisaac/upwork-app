package co.aisaac.upwork;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import org.jsoup.Jsoup;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Configuration
public class ParseRssScheduler {

    static class Job {
        String budget, category, skills, country, datetime, locationRequirements, hourlyRange;
    }

    @Scheduled(fixedDelay = 5 * 1000)
    public void run() throws IOException {
        String url = "https://www.upwork.com/ab/feed/jobs/rss?category2_uid=531770282580668418" +
                "&contractor_tier=2%2C3&hourly_rate=40-&paging=NaN-undefined" +
                "&sort=recency&t=0&api_params=1&securityToken=f2b1425b06ba5e895d96d6a621a0551c9cb3a8bee728999cb662cb574696abcffa77f426df80aff253a7028422fdcda0c91ff972aede682c39c4300e3bed2801" +
                "&userUid=1299200877346168832&orgUid=1299200877350363137";

        RssReader rssReader = new RssReader();
        List<Item> items = rssReader.read(url).toList();

        List<String> texts = items.stream()
                .map(Item::getDescription)
                .filter(Optional::isPresent)
                .map(s -> Jsoup.parse(s.get()).wholeText())
                .map(t -> t.replaceAll("\n\n", "\n"))
                .toList();


        texts.forEach(System.out::println);
    }

}
