package co.aisaac.upwork;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Entity(name = "postings")
@Table(name = "postings", schema = "upwork")
public class Posting {

    @Id
    @SequenceGenerator(name = "postings_id_generator", sequenceName = "postings_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postings_id_generator")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    public String url;
    public String budget;
    public String category;
    public String skills;
    public String country;
    @Column(name = "location_requirements")
    public String locationRequirements;
    @Column(name = "hourly_range")
    public String hourlyRange;
    public String description;
    public LocalDateTime datetime;
}
