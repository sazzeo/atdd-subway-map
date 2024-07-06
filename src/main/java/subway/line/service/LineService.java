package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineStation;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;
import subway.line.payload.LineStationResponse;
import subway.line.repository.LineRepository;
import subway.line.repository.LineStationRepository;

import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final LineStationRepository lineStationRepository;

    public LineService(final LineRepository lineRepository, final LineStationRepository lineStationRepository) {
        this.lineRepository = lineRepository;
        this.lineStationRepository = lineStationRepository;
    }

    @Transactional
    public LineResponse saveLine(CreateLineRequest request) {
        Line line = lineRepository.save(request.toEntity());
        var upStation = new LineStation(line.getId(), request.getUpStationId());
        var downStation = new LineStation(line.getId(), request.getDownStationId());
        lineStationRepository.saveAll(List.of(upStation, downStation));
        var lineStations = lineStationRepository.findByLineId(line.getId()).stream().map(
                this::createLineStationResponse
        ).collect(Collectors.toList());

        return this.createLineResponse(line, lineStations);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(line -> this.createLineResponse(line, List.of()))
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(final Line line, final List<LineStationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private LineStationResponse createLineStationResponse(LineStation lineStation) {
        return new LineStationResponse(lineStation.getId(), "지하철역");
    }

}
