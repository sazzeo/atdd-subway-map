package subway.line.domain;

import subway.line.exception.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();

    public void add(final Section section) {
        if(this.sections.isEmpty()) {
            sections.add(section);
            return;
        }

        if(!hasStation(section.getUpStationId())) {
            throw new LineHasNoStationException("추가하려는 상행역이 기존 노선에 존재하지 않습니다.");
        }

        if(isUpStationAlreadyExists(section.getDownStationId())) {
            throw new InvalidDownStationException("하행역으로 등록하려는 역이 이미 존재합니다.");
        }
        sections.add(section);
    }

    private boolean hasStation(Long stationId) {
        List<Long> stationIds = getAllStationIds();
        return stationIds.contains(stationId);
    }

    public List<Long> getAllStationIds() {
        List<Long> stationIds = sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());

        stationIds.add(getLastDownStationId());
        return stationIds;
    }


    public boolean isNotLastStation(final Long stationId) {
        return !this.getLastDownStationId().equals(stationId);
    }

    public void removeLastStation(final Long stationId) {
        if(isNotLastStation(stationId)) {
            throw new NotTerminusStationException("삭제하려는 역이 종착역이 아닙니다.");
        }
        if(hasOnlyOneSection()) {
            throw new InsufficientStationsException("구간이 1개밖에 없어 역을 삭제할 수 없습니다.");
        }
        sections.remove(sections.size()-1);
    }

    public boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

    private Long getLastDownStationId() {
        return sections.get(sections.size() - 1).getDownStationId();
    }

    private boolean isUpStationAlreadyExists(Long stationId) {
        return sections.stream()
                .map(Section::getUpStationId)
                .anyMatch(id -> id.equals(stationId));
    }
}
