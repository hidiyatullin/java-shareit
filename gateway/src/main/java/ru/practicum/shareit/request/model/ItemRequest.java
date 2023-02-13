package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

//import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
//@Table(name = "requests")
public class ItemRequest {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(name = "description", nullable = false, length = 100)
    private String description;
//    @ManyToOne(cascade = {CascadeType.ALL})
//    @JoinColumn(name = "requester_id")
    private User requester;
//    @Column(name = "created")
    private LocalDateTime created;
}
