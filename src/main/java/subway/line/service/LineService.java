package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.payload.AddSectionRequest;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;
import subway.line.payload.UpdateLineRequest;
import subway.line.repository.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(final CreateLineRequest request) {

        var upStation = this.getStationById(request.getUpStationId());
        var downStation = this.getStationById(request.getDownStationId());

        Line line = lineRepository.save(
                new Line(request.getName(),
                        request.getColor(),
                        new Section(upStation.getId(), downStation.getId(), request.getDistance())
                ));

        return LineResponse.from(line, getLineStations(line));
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        Set<Long> stationsIds = lines.stream()
                .flatMap(line -> line.getStationIds().stream())
                .collect(Collectors.toSet());

        Map<Long, Station> stationMap = stationRepository.findByIdIn(stationsIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, (station -> station)));

        return lines.stream()
                .map(line ->
                        LineResponse.from(line,
                                stationsIds.stream()
                                        .map(stationMap::get)
                                        .collect(Collectors.toList()))
                ).collect(Collectors.toList());
    }

    public LineResponse getLineResponse(final Long id) {
        Line line = getLineById(id);
        return LineResponse.from(line, getLineStations(line));
    }


    @Transactional
    public void modify(final Long id, final UpdateLineRequest request) {
        var line = getLineById(id);
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void delete(final Long id) {
        var line = getLineById(id);
        lineRepository.delete(line);
    }

    @Transactional
    public void addSection(final Long id, final AddSectionRequest request) {
        var line = getLineById(id);
        var upStation = getStationById(request.getUpStationId());
        var downStation = getStationById(request.getDownStationId());
        line.addSection(upStation.getId(), downStation.getId(), request.getDistance());
    }

    private Station getStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }

    private Line getLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지하철 노선입니다."));
    }

    private List<Station> getLineStations(final Line line) {
        return stationRepository.findByIdIn(line.getStationIds());
    }

}
