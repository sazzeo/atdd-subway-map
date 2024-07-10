package subway.line.domain;

import subway.station.Station;

import javax.persistence.*;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    public Section() {

    }

    public Section(final Station upStation, final Station downStation, final Long distance) {
        this.upStationId = upStation.getId();
        this.downStationId = downStation.getId();
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
