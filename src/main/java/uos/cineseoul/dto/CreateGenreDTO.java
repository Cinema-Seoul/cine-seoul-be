package uos.cineseoul.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateGenreDTO {
    @NotBlank
    @Size(min = 2, max = 2)
    private String genreCode;

    @NotBlank
    @Size(max = 20)
    private String name;
}
