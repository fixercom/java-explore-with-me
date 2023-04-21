package ru.practicum.ewm.vote.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "user_votes")
public class UserVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long eventId;
    @Column(nullable = false)
    private Boolean isPositive;

}
