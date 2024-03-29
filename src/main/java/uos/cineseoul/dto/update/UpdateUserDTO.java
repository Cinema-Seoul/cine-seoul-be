package uos.cineseoul.dto.update;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Size;

@AllArgsConstructor()
@NoArgsConstructor(force = true)
@Setter
@Getter
@Builder
public class UpdateUserDTO {
    // 입력 비밀번호 자리수 제한
    @Size(max = 16, min = 0)
    private String pw;

    @Size(max = 100, min = 0)
    private String name;

    @NotNull
    @Size(max = 11, min = 10)
    private String phoneNum;
}
