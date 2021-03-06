package me.dongguri.demorestapi.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.dongguri.demorestapi.accounts.Account;
import me.dongguri.demorestapi.accounts.AccountSerializer;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Event extends RepresentationModel<Event> {
    @Id @GeneratedValue
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
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class) // Mapping 할 때 Custom Serializer 사용.
    private Account manager;

    public void update() {
        // update Free
        if (this.basePrice == 0 && this.maxPrice == 0 ) {
            this.free = true;
        } else {
            this.free = false;
        }

        // update offline (isBlank Java11)
        if(this.location == null || this.location.isBlank()) {
            this.offline = false;
        }else {
            this.offline = true;
        }
    }

}
