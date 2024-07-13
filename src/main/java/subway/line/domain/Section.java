package subway.line.domain;

import subway.line.exception.SectionDistanceNotValidException;
import subway.line.exception.SectionNotValidException;

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

    public Section(final Long upStationId, final Long downStationId, final Long distance) {
        validate(upStationId, downStationId, distance);
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validate(final Long upStationId, final Long downStationId, final Long distance) {
        if(upStationId.equals(downStationId)) {
            throw new SectionNotValidException("상행역과 하행역은 다른 역이어야 합니다.");
        }
        if(distance < 1) {
            throw new SectionDistanceNotValidException("거리는 1이상이어야 합니다.");
        }
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

    public void updateDownStationAndDistance(final Long downStationId,final Long newDistance) {
        this.downStationId = downStationId;
        this.distance = newDistance;
    }
}
