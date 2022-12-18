//package ru.practicum.shareit.item.dto;
//
//import lombok.*;
//import ru.practicum.shareit.Create;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.model.User;
//
//import javax.validation.constraints.NotBlank;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * TODO Sprint add-controllers.
// */
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
//@Builder
//public class ItemInfoDto {
//    private Long id;
//    private String name;
//    private String description;
//    private Boolean available;
//    private String request;
//    private BookingDto lastBooking;
//    private BookingDto nextBooking;
//    private List<CommentDto> comments = new ArrayList<>();
//
//
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Getter
//    @Setter
//    @Builder
//    public static class BookingDto {
//        Long id;
//        LocalDateTime start;
//        LocalDateTime end;
//        Long bookerId;
//    }
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class CommentDtoItem {
//        Long id;
//        String text;
//        Item item;
//        User author;
//    }
//}
