package ru.practicum.shareit.booking;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.time.Instant;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings", schema = "public")
public class Booking {
    @PositiveOrZero
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_date")
    private Instant start;
    @Column(name = "end_date")
    private Instant end;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "booker_id")
    private Long booker;
    @Enumerated(EnumType.STRING)
    private Status status;
}
