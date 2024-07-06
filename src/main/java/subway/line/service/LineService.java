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
import java.util.Map;
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
        this.saveLineStation(request, line.getId());
        var lineStations = getLineStationsByLineId(line.getId());

        return this.createLineResponse(line, lineStations);
    }


    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        Map<Long, List<LineStation>> lineMap = lineStationRepository.findByLineIdIn(lines.stream().map(Line::getId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.groupingBy(LineStation::getLineId));
        return lines.stream()
                .map(line -> this.createLineResponse(line, lineMap.get(line.getId())))
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(final Line line, final List<LineStation> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(),
                stations.stream()
                        .map(station -> new LineStationResponse(station.getId(), "지하철역"))
                        .collect(Collectors.toList()));
    }

    private void saveLineStation(final CreateLineRequest request, final Long lindId) {
        var upStation = new LineStation(lindId, request.getUpStationId());
        var downStation = new LineStation(lindId, request.getDownStationId());
        lineStationRepository.saveAll(List.of(upStation, downStation));
    }

    private List<LineStation> getLineStationsByLineId(final Long lineId) {
        return lineStationRepository.findByLineId(lineId);
    }

}
