package ru.practicum.shareit.item.model;


import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Entity;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */

//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class Item {
//    private Long id;
//    private String name;
//    private String description;
//    private Boolean available;
//    private User owner;
//    private String request;
//}
@Entity
@Table(name = "items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    private String description;
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    private String request;
}
