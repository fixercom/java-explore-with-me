package ru.practicum.ewm.event.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.Location;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000, nullable = false, unique = true)
    private String annotation;
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Category category;
    @Column(length = 7000, nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime eventDate;
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "location_latitude", nullable = false)),
            @AttributeOverride(name = "lon", column = @Column(name = "location_longitude", nullable = false))
    })
    private Location location;
    @Column(nullable = false)
    private Boolean paid;
    @Column(nullable = false)
    private Integer participantLimit;
    @Column(nullable = false)
    private Integer confirmedRequests;
    @Column(nullable = false)
    private Boolean requestModeration;
    @Column(length = 120, nullable = false)
    private String title;
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private User initiator;
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private EventState state;
    @Column(name = "date_created", nullable = false)
    private LocalDateTime createdOn;
}