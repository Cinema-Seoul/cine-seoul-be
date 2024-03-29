package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertDirectorDTO;
import uos.cineseoul.entity.movie.Actor;
import uos.cineseoul.entity.movie.Director;
import uos.cineseoul.entity.movie.Distributor;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.DirectorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectorService {

    private final DirectorRepository directorRepository;
    public List<Director> findDirectorList() {
        List<Director> directorList = directorRepository.findAll();
        return directorList;
    }

    public Page<Director> findDirectorList(Pageable pageable) {
        Page<Director> directorList = directorRepository.findAll(pageable);
        return directorList;
    }
    public Director findDirector(Long dirNum) {
        Director director = directorRepository.findByDirNum(dirNum).orElseThrow(
                () -> new ResourceNotFoundException("해당 번호의 감독이 없습니다.")
        );
        return director;
    }

    public Director insert(InsertDirectorDTO insertDirectorDTO) {
        Director director = Director
                .builder()
                .name(insertDirectorDTO.getName())
                .imgUrl(insertDirectorDTO.getImgUrl())
                .build();
        return directorRepository.save(director);
    }
}
