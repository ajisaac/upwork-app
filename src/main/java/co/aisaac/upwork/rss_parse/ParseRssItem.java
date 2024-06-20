package co.aisaac.upwork.rss_parse;

import co.aisaac.upwork.model.Posting;
import com.apptasticsoftware.rssreader.Item;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

class ParseRssItem {
    private static final Logger log = LoggerFactory.getLogger(ParseRssItem.class);

    static Posting parse(Item item) {
        Posting posting = new Posting();
        Optional<String> description = item.getDescription();

        if (description.isPresent()) {
            String t = Jsoup.parse(description.get()).wholeText();
            String text = t.replaceAll("\n\n", "\n");
            String[] lines = text.split("\n");

            for (String line : lines) {

                try {

                    if (line.trim().equals("Budget:")) {
                        posting.budget = "";
                    } else if (line.startsWith("Budget:")) {
                        posting.budget = line.substring(8).trim();
                    } else if (line.startsWith("Hourly Range:")) {
                        posting.hourlyRange = line.substring(13).trim();
                    } else if (line.startsWith("Category:")) {
                        posting.category = line.substring(10).trim();
                    } else if (line.startsWith("Posted On:")) {
                        try {
                            posting.datetime = LocalDateTime.parse(line.substring(11).trim());
                        } catch (Exception ex) {
                            posting.datetime = LocalDateTime.now();
                        }
                    } else if (line.startsWith("Country:")) {
                        posting.country = line.substring(9).trim();
                    } else if (line.startsWith("Skills:")) {
                        String s = line.substring(7).trim();
                        posting.skills = Arrays
                                .stream(s.split(","))
                                .map(String::trim)
                                .collect(Collectors.joining(", "));
                    }
                } catch (StringIndexOutOfBoundsException ex) {
                    log.error("Out of bounds for line: {}", line, ex);
                }

            }
            posting.description = text;
            posting.htmlDescription = description.get();
        } else {
            log.warn("Unable to find description on RSS feed item.");
            return null;
        }

        if (item.getTitle().isPresent()) {
            posting.title = item.getTitle().get();
        } else {
            posting.title = "Unknown title.";
            log.warn("Unable to find title on RSS feed item.");
        }

        if (item.getGuid().isPresent()) {
            posting.guid = item.getGuid().get();
        } else {
            log.warn("Unable to find GUID on RSS feed item.");
            return null;
        }

        if (item.getPubDate().isPresent()) {
            try {
                posting.pubDate = LocalDateTime.parse(item.getPubDate().get());
            } catch (Exception ex) {
                posting.pubDate = LocalDateTime.now();
            }
        } else {
            log.warn("Unable to find PubDate on RSS feed item.");
        }

        if (item.getLink().isPresent()) {
            posting.url = item.getLink().get();
        } else {
            log.warn("Unable to find URL for RSS feed item.");
            return null;
        }

        posting.status = "new";

        return posting;
    }
}
