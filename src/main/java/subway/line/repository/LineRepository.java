package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.line.domain.Line;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l from Line l join l.upStation join fetch l.downStation")
    List<Line> findAllJoinLine();

}
