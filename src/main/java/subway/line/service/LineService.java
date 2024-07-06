package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;
import subway.line.repository.LineRepository;
import subway.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(CreateLineRequest request) {
        Line line = lineRepository.save(request.toEntity());
        List<StationResponse> stations = createStations(request);

        return this.createLineResponse(line, stations);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(line -> this.createLineResponse(line, List.of()))
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(final Line line, final List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private List<StationResponse> createStations(final CreateLineRequest request) {
        return List.of(new StationResponse(request.getUpStationId(), "시청"),
                new StationResponse(request.getDownStationId(), "충정로"));
    }
}
