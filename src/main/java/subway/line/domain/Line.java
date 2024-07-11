package subway.line.domain;

import subway.line.exception.InvalidDownStationException;
import subway.line.exception.InvalidUpStationException;
import subway.line.exception.NotTerminusStationException;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(final String name, final String color, final Section section) {
        this.name = name;
        this.color = color;
        sections.add(section);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }


    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(final Long upStationId, final Long downStationId, final Long distance) {
        if (!sections.isLastStation(upStationId)) {
            throw new InvalidUpStationException("새로등록하려는 상행역이 기존 하행역이 아닙니다.");
        }

        if (sections.isUpStationAlreadyExists(downStationId)) {
            throw new InvalidDownStationException("하행역으로 등록하려는 역이 이미 존재합니다.");
        }
        sections.add(new Section(upStationId, downStationId, distance));
    }

    public List<Long> getStationIds() {
        return sections.getStationIds();
    }

    public void removeLastStation(final Long stationId) {
        if(!sections.isLastStation(stationId)) {
            throw new NotTerminusStationException("삭제하려는 역이 종착역이 아닙니다.");
        }
        sections.removeLastStation();
    }
}
