package subway.line.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long lineId;
    private long stationId;

    public LineStation() {
    }

    public LineStation(final long lineId, final long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
    }

    public long getLineId() {
        return lineId;
    }

    public long getStationId() {
        return stationId;
    }
}
