package uos.cineseoul.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@IdClass(MovieGenreId.class)
public class MovieGenre {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_NUM")
    private Movie movie;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GENRE_CODE")
    private Genre genre;

    @Builder
    public MovieGenre(Movie movie, Genre genre) {
        this.movie = movie;
        this.genre = genre;
        movie.getMovieGenreList().add(this);
        genre.getMovieGenreList().add(this);
    }
}
