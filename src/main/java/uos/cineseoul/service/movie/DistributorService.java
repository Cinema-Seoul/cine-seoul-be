package uos.cineseoul.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertDistributorDTO;
import uos.cineseoul.entity.movie.Distributor;
import uos.cineseoul.entity.movie.Genre;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.DistributorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DistributorService {

    private final DistributorRepository distributorRepository;

    public List<Distributor> findDistributorList() {
        List<Distributor> distributorList = distributorRepository.findAll();
        return distributorList;
    }

    public Page<Distributor> findDistributorList(Pageable pageable) {
        Page<Distributor> distributorList = distributorRepository.findAll(pageable);
        return distributorList;
    }

    public Distributor findDistributor(Long distNum) {
        Distributor distributor = distributorRepository.findByDistNum(distNum).orElseThrow(
                () -> new ResourceNotFoundException("해당 번호의 배급사가 없습니다.")
        );
        return distributor;
    }

    public Distributor insert(InsertDistributorDTO insertDistributorDTO) {
        Distributor distributor = Distributor
                .builder()
                .name(insertDistributorDTO.getName())
                .build();
        return distributorRepository.save(distributor);
    }
}
