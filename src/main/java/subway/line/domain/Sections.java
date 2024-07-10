package subway.line.domain;

import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();

    public void add(final Section section) {
        this.sections.add(section);
    }

    public Long getLastDownStationId() {
        return sections.get(sections.size() - 1).getDownStationId();
    }

    public boolean isUpStationAlreadyExists(Long stationId) {
        return sections.stream()
                .map(Section::getUpStationId)
                .anyMatch(id -> id.equals(stationId));
    }

}
