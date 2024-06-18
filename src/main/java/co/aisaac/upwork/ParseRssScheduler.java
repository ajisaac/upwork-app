package co.aisaac.upwork;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class ParseRssScheduler {

    static class Job {
        String budget, category, skills, country, datetime, locationRequirements, hourlyRange, description;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Budget: " + budget + "\n");
            sb.append("Category: " + category + "\n");
            sb.append("Skills: " + skills + "\n");
            sb.append("Country: " + country + "\n");
            sb.append("Datetime: " + datetime + "\n");
            sb.append("LocationRequirements: " + locationRequirements + "\n");
            sb.append("HourlyRange: " + hourlyRange + "\n");
            return sb.toString();
        }
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

        List<Job> jobs = new ArrayList<>();

        for (String text : texts) {
            Job job = new Job();
            String[] lines = text.split("\n");

            for (String line : lines) {
                if (line.startsWith("Budget:")) {
                    job.budget = line.substring(8).trim();
                } else if (line.startsWith("Hourly Range:")) {
                    job.hourlyRange = line.substring(13).trim();
                } else if (line.startsWith("Category:")) {
                    job.category = line.substring(10).trim();
                } else if (line.startsWith("Posted On:")) {
                    job.datetime = line.substring(11).trim();
                } else if (line.startsWith("Country:")) {
                    job.country = line.substring(9).trim();
                } else if (line.startsWith("Skills:")) {
                    String s = line.substring(7).trim();
                    String clean = Arrays.stream(s.split(",")).map(String::trim).collect(Collectors.joining(", "));
                    if (job.skills != null) {
                        job.skills = job.skills + ", " + clean;
                    } else {
                        job.skills = clean;
                    }
                }
            }

            job.description = text;
            jobs.add(job);
        }

        for (Job job : jobs) {
            System.out.println(job);
            System.out.println("\n");
        }
    }
}
