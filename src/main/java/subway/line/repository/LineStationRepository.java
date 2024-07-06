package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.LineStation;

import java.util.List;

public interface LineStationRepository extends JpaRepository<LineStation, Long> {
    List<LineStation> findByLineId(Long lineId);

    List<LineStation> findByLineIdIn(List<Long> lineIds);
}
