package uos.cineseoul.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.complex.CancelRegisterTicketDTO;
import uos.cineseoul.dto.create.CreateTicketDTO;
import uos.cineseoul.dto.fix.FixTicketDTO;
import uos.cineseoul.dto.insert.InsertReservationSeatDTO;
import uos.cineseoul.dto.insert.InsertTicketDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.entity.ScheduleSeat;
import uos.cineseoul.entity.Ticket;
import uos.cineseoul.service.ScheduleService;
import uos.cineseoul.service.TicketService;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.PageUtil;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.utils.enums.TicketState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController()
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @GetMapping("/auth")
    @ApiOperation(value = "전체 티켓 목록 조회 (filter : userNum)", protocols = "http")
    public ResponseEntity<PrintPageDTO<PrintTicketDTO>> lookUpTicketList(@RequestParam(value="userNum", required = false) Long userNum,
                                                         @RequestParam(value="sort_created_date", required = false ) boolean isSortCreatedDate,
                                                         @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                                         @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                                         @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        String sortBy = isSortCreatedDate ? "createdAt" :"ticketNum";
        Pageable pageable = PageUtil.setPageable(page, size,sortBy,sortDir);

        Page<Ticket> ticketList;
        if(userNum!=null){
            ticketList = ticketService.findByUserNum(userNum, pageable);
        }else{
            ticketList = ticketService.findAll(pageable);
        }
        List<PrintTicketDTO> printTicketDTOS = ticketService.toPrintDTOList(ticketList.getContent());
        return new ResponseEntity<>(new PrintPageDTO<>(printTicketDTOS,ticketList.getTotalPages()), HttpStatus.OK);
    }

    @GetMapping("/auth/{num}")
    @ApiOperation(value = "티켓 번호로 조회", protocols = "http")
    public ResponseEntity<PrintTicketDTO> lookUpTicketByNum(@PathVariable("num") Long num) {
        Ticket ticket = ticketService.findOneByNum(num);

        return new ResponseEntity<>(ticketService.toPrintDTO(ticket), HttpStatus.OK);
    }

    @PostMapping("/auth")
    @ApiOperation(value = "티켓 등록", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintTicketDTO>> register(@RequestBody CreateTicketDTO ticketDTO) {
        AtomicReference<Integer> totalPrice = new AtomicReference<>(0);
        List<ScheduleSeat> scheduleSeatList = new ArrayList<>();
        List<InsertReservationSeatDTO> insertReservationSeatDTOS = new ArrayList<>();
        fillInsertReservationSeatDTO(insertReservationSeatDTOS, scheduleSeatList, ticketDTO.getSeatNumList(), ticketDTO.getSchedNum(), totalPrice);

        InsertTicketDTO iTicketDTO = InsertTicketDTO.builder().ticketState(TicketState.N).schedule(scheduleService.findOneByNum(ticketDTO.getSchedNum()))
                                                            .user(userService.findOneByNum(ticketDTO.getUserNum())).stdPrice(totalPrice.get()).build();
        Ticket ticket = ticketService.insert(iTicketDTO, insertReservationSeatDTOS, scheduleSeatList, ticketDTO.getAudienceTypeList());
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 예매가 완료되었습니다.");
        msg.setData(ticketService.toPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/auth")
    @ApiOperation(value = "티켓 정보 변경", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintTicketDTO>> update(@RequestBody FixTicketDTO ticketDTO) {
        Ticket ticket = ticketService.update(ticketDTO.getTicketNum(), ticketDTO.toUpdateDTO());
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 변경이 완료되었습니다.");
        msg.setData(ticketService.toPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/auth/{num}")
    @ApiOperation(value = "비회원 티켓 삭제 by 티켓 번호", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintTicketDTO>> delete(@PathVariable("num") Long num) {
        ticketService.delete(num);
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 삭제가 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/auth/cancelregister")
    @Transactional
    @ApiOperation(value = "티켓 취소 및 등록", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintTicketDTO>> CancelAndRegister(@RequestBody CancelRegisterTicketDTO ticketDTO) {
        AtomicReference<Integer> totalPrice = new AtomicReference<>(0);
        List<ScheduleSeat> scheduleSeatList = new ArrayList<>();
        List<InsertReservationSeatDTO> insertReservationSeatDTOS = new ArrayList<>();
        fillInsertReservationSeatDTO(insertReservationSeatDTOS, scheduleSeatList, ticketDTO.getSeatNumList(), ticketDTO.getSchedNum(), totalPrice);

        Ticket ticket = ticketService.cancelAndChangeSeat(ticketDTO.getTicketNum(), ticketDTO.toUpdateDTO(totalPrice),userService.findOneByNum(ticketDTO.getUserNum()), insertReservationSeatDTOS);
        ReturnMessage<PrintTicketDTO> msg = new ReturnMessage<>();
        msg.setMessage("티켓 취소 및 등록이 완료되었습니다.");
        msg.setData(ticketService.toPrintDTO(ticket));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    private void fillInsertReservationSeatDTO(List<InsertReservationSeatDTO> insertReservationSeatDTOS, List<ScheduleSeat> scheduleSeatList,
                                            List<Long> seatNumList, Long schedNum, AtomicReference<Integer> totalPrice){
        seatNumList.forEach(seatNum -> {
            ScheduleSeat scheduleSeat = scheduleService.findScheduleSeat(schedNum,seatNum);
            totalPrice.updateAndGet(v -> v + scheduleSeat.getSeat().getSeatGrade().getPrice());
            insertReservationSeatDTOS.add(InsertReservationSeatDTO.builder().seat(scheduleSeat.getSeat()).build());
            scheduleSeatList.add(scheduleSeat);
        });
    }

//    private void fillInsertReservationDTO(List<SeatTypeDTO> seatTypeDTOS, Long schedNum, AtomicReference<Integer> totalPrice){
//        List<InsertReservationSeatDTO> insertReservationSeatDTOS = new ArrayList<>();
//        seatTypeDTOS.forEach(seatTypeDTO -> {
//            ScheduleSeat scheduleSeat = scheduleService.findScheduleSeat(schedNum,seatTypeDTO.getSeatNum());
//            totalPrice.updateAndGet(v -> v + scheduleSeat.getSeat().getSeatGrade().getPrice());
//            insertReservationSeatDTOS.add(InsertReservationSeatDTO.builder().scheduleSeat(scheduleSeat).audienceType(seatTypeDTO.getAudienceType()).build());
//        });
//        return insertReservationSeatDTOS;
//    }
}