package uos.cineseoul.controller;


import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uos.cineseoul.dto.insert.InsertUserDTO;
import uos.cineseoul.dto.request.LoginDTO;
import uos.cineseoul.dto.request.LoginNotMemberDTO;
import uos.cineseoul.dto.response.PrintPageDTO;
import uos.cineseoul.dto.response.PrintTicketDTO;
import uos.cineseoul.dto.response.PrintUserDTO;
import uos.cineseoul.dto.update.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.service.UserService;
import uos.cineseoul.utils.JwtTokenProvider;
import uos.cineseoul.utils.PageUtil;
import uos.cineseoul.utils.ReturnMessage;
import uos.cineseoul.utils.enums.StatusEnum;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/admin")
    @ApiOperation(value = "전체 사용자 목록 조회", protocols = "http")
    public ResponseEntity<PrintPageDTO<PrintUserDTO>> lookUpUserList(@RequestParam(value="sort_name", required = false, defaultValue = "0") boolean isSortName,
                                             @RequestParam(value="sort_dir", required = false) Sort.Direction sortDir,
                                             @RequestParam(value="page", required = false, defaultValue = "0") int page,
                                             @RequestParam(value="size", required = false, defaultValue = "12") int size) {
        String sortBy = isSortName ? "name" :"userNum";
        Pageable pageable = PageUtil.setPageable(page, size,sortBy,sortDir);

        Page<User> userList = userService.findAll(pageable);
        List<PrintUserDTO> printUserDTOS = userService.getPrintDTOList(userList.getContent());
        return new ResponseEntity<>(new PrintPageDTO<>(printUserDTOS,userList.getTotalPages()), HttpStatus.OK);
    }

    @GetMapping("/admin/{num}")
    @ApiOperation(value = "사용자 번호로 조회", protocols = "http")
    public ResponseEntity<PrintUserDTO> lookUpUserByNum(@PathVariable(value = "num") Long num) {
        User user = userService.findOneByNum(num);

        return new ResponseEntity<>(userService.getPrintDTO(user), HttpStatus.OK);
    }

    @GetMapping("/auth")
    @ApiOperation(value = "사용자 JWT헤더로 조회", protocols = "http")
    public ResponseEntity<PrintUserDTO> lookUpUser(@RequestHeader(value = "Authorization") String token) {
        Long userNum = jwtTokenProvider.getClaims(token).get("num", Long.class);
        User user = userService.findOneByNum(userNum);

        return new ResponseEntity<>(userService.getPrintDTO(user), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "사용자 회원가입", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintUserDTO>> register(@RequestBody @Valid InsertUserDTO userDTO) {
        User user = userService.insert(userDTO);
        ReturnMessage<PrintUserDTO> msg = new ReturnMessage<>();
        msg.setMessage("회원가입이 완료되었습니다.");
        msg.setData(userService.getPrintDTO(user));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation(value = "회원 로그인", protocols = "http")
    public ResponseEntity<ReturnMessage<String>> login(@RequestBody @Valid LoginDTO loginInfo) {
        User user = userService.login(loginInfo);
        ReturnMessage<String> msg = new ReturnMessage<>();
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().toString());
        String token = jwtTokenProvider.createToken(user.getUserNum(),user.getId(), user.getName(),roles);
        msg.setMessage("로그인이 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);
        msg.setData(token);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/login/notMember")
    @ApiOperation(value = "비회원 로그인", protocols = "http")
    public ResponseEntity<ReturnMessage<String>> loginNotMember(@RequestBody @Valid LoginNotMemberDTO loginInfo) {
        User user = userService.loginNotMember(loginInfo);
        ReturnMessage<String> msg = new ReturnMessage<>();
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().toString());
        String token = jwtTokenProvider.createToken(user.getUserNum(),"",user.getName(),roles);
        msg.setMessage("로그인이 완료되었습니다.");
        msg.setStatus(StatusEnum.OK);
        msg.setData(token);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    
    @PutMapping("/auth")
    @ApiOperation(value = "사용자 정보 변경 by JWT 헤더", protocols = "http")
    public ResponseEntity<ReturnMessage<PrintUserDTO>> update(@RequestHeader(value = "Authorization") String token, @RequestBody UpdateUserDTO userDTO) {
        Long userNum = jwtTokenProvider.getClaims(token).get("num", Long.class);
        User user = userService.update(userNum, userDTO);
        ReturnMessage<PrintUserDTO> msg = new ReturnMessage<>();
        msg.setMessage("사용자 정보 변경이 완료되었습니다.");
        msg.setData(userService.getPrintDTO(user));
        msg.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}