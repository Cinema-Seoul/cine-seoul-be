package uos.cineseoul.dto.update;

import lombok.Data;
import uos.cineseoul.dto.fix.FixMovieDTO;
import uos.cineseoul.utils.ActorAndRole;
import uos.cineseoul.utils.enums.Is;

import java.util.List;

@Data
public class UpdateMovieDTO {

    private Long movie_num;
    private String title;

    private String info;

    private String releaseDate;

    private Integer runningTime;

    private Is isShowing;

    private Long distNum;

    private String poster;

    private String gradeCode;

    private List<String> genreList;

    private List<ActorAndRole> actorList;

    private List<Long> directorList;

    private List<String> countryList;

    public UpdateMovieDTO(FixMovieDTO fixMovieDTO) {
        this.movie_num = fixMovieDTO.getMovie_num();
        this.title = fixMovieDTO.getTitle();
        this.poster = fixMovieDTO.getPoster();
        this.info = fixMovieDTO.getInfo();
        this.releaseDate = fixMovieDTO.getReleaseDate();
        this.runningTime = fixMovieDTO.getRunningTime();
        this.isShowing = fixMovieDTO.getIsShowing();
        this.distNum = fixMovieDTO.getDistNum();
        this.gradeCode = fixMovieDTO.getGradeCode();
        this.genreList = fixMovieDTO.getGenreCodeList();
        this.actorList = fixMovieDTO.getActorNumList();
        this.directorList = fixMovieDTO.getDirectorNumList();
        this.countryList = fixMovieDTO.getCountryList();
    }
}
