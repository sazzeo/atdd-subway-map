package subway.line.domain;

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
        sections.add(section);
    }

    private Long getLastDownStationId() {
        return sections.get(sections.size() - 1).getDownStationId();
    }

    public boolean isUpStationAlreadyExists(Long stationId) {
        return sections.stream()
                .map(Section::getUpStationId)
                .anyMatch(id -> id.equals(stationId));
    }

    public List<Long> getStationIds() {
        List<Long> stationIds = sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());

        stationIds.add(getLastDownStationId());
        return stationIds;
    }

    public boolean isLastStation(final Long stationId) {
        return this.getLastDownStationId().equals(stationId);
    }

    public void removeLastStation() {
        sections.remove(sections.size()-1);
    }

    public boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }
}
