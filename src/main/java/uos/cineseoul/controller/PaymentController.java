package uos.cineseoul.controller;


import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.create.CreatePaymentDTO;
import uos.cineseoul.dto.insert.InsertPaymentDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.dto.response.PrintPaymentDTO;
import uos.cineseoul.entity.Payment;
import uos.cineseoul.service.PaymentService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.JwtTokenProvider;
import uos.cineseoul.utils.PageUtil;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import java.util.List;

@RestController()
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final TicketService ticketService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/auth")
    @ApiOperation(value = "전체 결제내역 목록 조회 (filter : userNum)", protocols = "http")
    public ResponseEntity<PrintPageDTO<PrintPaymentDTO>> lookUpPaymentList(@RequestParam(value="userNum", required = false) Long userNum,
                                                          @RequestParam(value="sort_created_date", required = false, defaultValue = "1") boolean isSortCreatedDate,
                                                          @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                          @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                          @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        String sortBy = isSortCreatedDate ? "createdAt" :"paymentNum";
        Pageable pageable = PageUtil.setPageable(page, size,sortBy,sortDir);

        Page<Payment> paymentList;
        if(userNum!=null){
            paymentList = paymentService.findByUserNum(userNum, pageable);
        }else{
            paymentList = paymentService.findAll(pageable);
        }
        List<PrintPaymentDTO> printPaymentDTOS = paymentService.getPrintDTOList(paymentList.getContent());
        return new ResponseEntity<>(new PrintPageDTO<>(printPaymentDTOS,paymentList.getTotalPages()), HttpStatus.OK);
    }

    @GetMapping("/auth/{num}")
    @ApiOperation(value = "결제내역 번호로 조회", protocols = "http")
    public ResponseEntity<PrintPaymentDTO> lookUpPaymentByNum(@PathVariable("num") Long num) {
        Payment payment = paymentService.findOneByNum(num);

        return new ResponseEntity<>(paymentService.getPrintDTO(payment), HttpStatus.OK);
    }

    @PostMapping("/auth")
    @ApiOperation(value = "결제내역 등록", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintPaymentDTO>> register(@RequestHeader(value = "Authorization") String token, @RequestBody CreatePaymentDTO paymentDTO) {
        Long userNum = jwtTokenProvider.getClaims(token).get("num", Long.class);
        InsertPaymentDTO insertPaymentDTO = new InsertPaymentDTO(paymentDTO, userService.findOneByNum(userNum),ticketService.findOneByNum(paymentDTO.getTicketNum()));
        Payment payment = paymentService.insert(insertPaymentDTO);
        ReturnMessage<PrintPaymentDTO> msg = new ReturnMessage<>();
        msg.setMessage("결제내역 등록이 완료되었습니다.");
        msg.setData(paymentService.getPrintDTO(payment));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}