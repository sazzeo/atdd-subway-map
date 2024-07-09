package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineStation;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;
import subway.line.payload.UpdateLineRequest;
import subway.line.repository.LineRepository;
import subway.line.repository.LineStationRepository;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final LineStationRepository lineStationRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final LineStationRepository lineStationRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.lineStationRepository = lineStationRepository;
        this.stationRepository = stationRepository;
    }


    @Transactional
    public LineResponse saveLine(final CreateLineRequest request) {

        var upStation = this.getStationById(request.getUpStationId());
        var downStation = this.getStationById(request.getDownStationId());

        Line line = lineRepository.save(
                new Line(request.getName(),
                        request.getColor(),
                        upStation,
                        downStation,
                        request.getDistance()));

        return LineResponse.from(line);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAllJoinLine().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse getById(final Long id) {
        var line = getLineById(id);
        var stations = lineStationRepository.findByLineId(line.getId());
        return this.createLineResponse(line, stations);
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

    private Station getStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }

    private Line getLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지하철 노선입니다."));
    }

    private LineResponse createLineResponse(final Line line, final List<LineStation> stations) {
        return null;
//        return new LineResponse(line.getId(), line.getName(), line.getColor(),
//                stations.stream()
//                        .map(station ->
//                                new StationResponse())
//                        .collect(Collectors.toList()));
//        return new LineResponse(line.getId(), line.getName(), line.getColor(),
//                stations.stream()
//                        .map(station -> new LineStationResponse(station.getId(), "지하철역"))
//                        .collect(Collectors.toList()));
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
