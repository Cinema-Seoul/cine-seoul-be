package uos.cineseoul.dto.insert;

import lombok.*;
import uos.cineseoul.entity.Schedule;
import uos.cineseoul.entity.User;
import uos.cineseoul.utils.enums.TicketState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class InsertTicketDTO {
    private Integer stdPrice;

    private Integer salePrice;

    @Enumerated(EnumType.STRING)
    private TicketState ticketState;

    private Schedule schedule;

    private User user;
}
