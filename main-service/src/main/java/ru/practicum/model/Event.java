package ru.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.EventStat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpHeaders.DATE;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Transient
    private final String datePattern = DATE;
    @Transient
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String annotation;
    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    private int confirmedRequests;
    @Column(name = "created_On")
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    @OneToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventStat state;
    private String title;
    private Long views;
}
