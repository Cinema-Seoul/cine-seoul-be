package uos.cineseoul.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import uos.cineseoul.utils.enums.PayState;
import uos.cineseoul.utils.enums.PaymentMethod;

@Entity(name = "PAYMENT")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Payment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PAYMENT_NUM")
    private Long paymentNum;

    @Column(name="APPROVAL_NUM", nullable = true, unique = false, length = 30)
    private String approvalNum ;

    @Column(name="PRICE", nullable = false, unique = false)
    private int price;

    @Column(name="STATE", columnDefinition = "char(1)")
    @Enumerated(EnumType.STRING)
    private PayState state;

    /* NEW */
    @Column(name="ACCOUNT_NUM", nullable = true, unique = false, length = 30)
    private String accountNum;

    @Column(name="CARD_NUM", nullable = true, unique = false, length = 16)
    private String cardNum;
    /* */

    @Column(name = "PAMENT_METHOD", nullable = false, length = 4)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    @Column(name="PAYED_DATE", nullable = false)
    private LocalDateTime createdAt;

    /* Foreign Key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NUM", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_NUM", nullable = false)
    private Ticket ticket;
    /* */
}
