package uos.cineseoul;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uos.cineseoul.dto.InsertUserDTO;
import uos.cineseoul.dto.UpdateUserDTO;
import uos.cineseoul.entity.User;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.mapper.UserMapper;
import uos.cineseoul.repository.UserRepository;
import uos.cineseoul.service.UserService;

import javax.transaction.Transactional;

@SpringBootTest
@Slf4j
class UserServiceTests {
	@Autowired
	UserService userService;
	@Test
	@Transactional
	void registerTest() {

		InsertUserDTO userDTO = InsertUserDTO.builder().id("sem13081").pw("1308").name("한수한")
				.residentNum("9902211111111").phoneNum("010XXXXXXXX").role("M").build();

		User savedUser = userService.insert(userDTO);
	}

	@Test
	@Transactional
	void updateTest() {
		Long userNum = 1L;
		String pw = "1308111";
		UpdateUserDTO userDTO = UpdateUserDTO.builder().userNum(userNum).pw(pw).name("한두한")
				.phoneNum("011XXXXXXXX").build();

		User updatedUser = userService.update(userDTO);

		// 변경된 비밀번호 확인
		System.out.println(updatedUser.getPw());
		assert userService.checkPassword(pw,updatedUser.getPw());
	}

	@Test
	@Transactional
	void findTest() {
		String ID = "sem1308";

		User user = userService.findOneById(ID);
	}

}