package ru.practicum.ewm.user.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer rate;

}
