package ru.practicum.ewm.stats.server.hit.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.ewm.stats.server.app.model.App;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "endpoint_hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private App app;
    @Column(length = 1000, nullable = false)
    private String uri;
    @Column(length = 15, nullable = false)
    private String ip;
    @Column(nullable = false)
    private LocalDateTime timestamp;

}
