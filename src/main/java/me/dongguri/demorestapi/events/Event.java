package me.dongguri.demorestapi.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@Getter
@Setter
public class Event {

    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment; // (optional)
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus;

}
