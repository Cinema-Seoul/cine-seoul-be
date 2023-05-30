package uos.cineseoul.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class PrintPaymentDTO {
    private Long paymentNum;

    private String approvalNum ;

    private int price;

    private LocalDateTime createdAt;

    private Long ticketNum;

    private PrintPaymentMethodDTO paymentMethod;
}