//package ru.practicum.shareit.booking;
//
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.model.User;
//
//import javax.persistence.Entity;
//import javax.persistence.Enumerated;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.validation.constraints.Future;
//import javax.validation.constraints.FutureOrPresent;
//import java.time.LocalDateTime;
//
///**
// * TODO Sprint add-bookings.
// */
//@Entity
//@Table(name = "bookings")
//public class Booking {
//    @Id
//    private long id;
//    @FutureOrPresent
//    private LocalDateTime start;
//    @Future
//    private LocalDateTime end;
//    private Item item;
//    private User booker;
//    @Enumerated
//    private BookingState status;
//
//}
