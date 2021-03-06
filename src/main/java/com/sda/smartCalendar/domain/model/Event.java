
package com.sda.smartCalendar.domain.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
public class Event {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    private UUID id;


    @Column
    private String name;

    @Column
    private String description;

    @Column
    private LocalDateTime event_start;

    @Column
    private LocalDateTime event_finish;

    @ManyToOne
    @JoinColumn (name="user_email")
    private User user;

    @Column
    private Category category;
}
