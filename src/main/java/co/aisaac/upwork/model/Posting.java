package co.aisaac.upwork.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "postings")
@Table(name = "postings", schema = "upwork")
public class Posting {

    @Id
    @SequenceGenerator(name = "postings_id_generator", sequenceName = "upwork.postings_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postings_id_generator")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    public String url;
    public String title;
    public String budget;
    public String category;
    public String skills;
    public String country;

    @Column(name = "location_requirements")
    public String locationRequirements;

    @Column(name = "hourly_range")
    public String hourlyRange;
    public String description;

    @Column(name = "html_description")
    public String htmlDescription;

    @Transient
    public String shortDescription;

    public LocalDateTime datetime;

    @Column(name = "pub_date")
    public LocalDateTime pubDate;

    // for unique check
    public String guid;

    public String status;

    @Override
    public String toString() {
        return "Posting{" +
                "id=" + id + ",\n" +
                "url='" + url + "',\n" +
                "status='" + status + "',\n" +
                "title='" + title + "',\n" +
                "budget='" + budget + "',\n" +
                "category='" + category + "',\n" +
                "skills='" + skills + "',\n" +
                "country='" + country + "',\n" +
                "locationRequirements='" + locationRequirements + "',\n" +
                "hourlyRange='" + hourlyRange + "',\n" +
                "datetime=" + datetime + ",\n" +
                "pubDate=" + pubDate + ",\n" +
                "guid='" + guid + "'" + ",\n" +
                '}';
    }
}
