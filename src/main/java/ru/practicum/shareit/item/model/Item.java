package ru.practicum.shareit.item.model;


import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Entity;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */

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
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false, length = 100)
    private String description;
    @Column(name = "is_available", nullable = false)
    private Boolean available;
    @OneToOne(orphanRemoval = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "owner_id")
    private User owner;
    @Transient
    private String request;
}
