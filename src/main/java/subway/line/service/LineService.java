package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;
import subway.line.repository.LineRepository;
import subway.station.StationResponse;

import java.util.List;


@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(CreateLineRequest request) {
        Line line = lineRepository.save(request.toEntity());
        List<StationResponse> stations = List.of(new StationResponse(request.getUpStationId(), "시청") ,
                new StationResponse(request.getDownStationId() , "충정로"));

        return new LineResponse(line.getId(), line.getName() , line.getColor() , stations);
    }


}
