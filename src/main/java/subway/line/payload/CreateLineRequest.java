package subway.line.payload;

import subway.line.domain.Line;

public class CreateLineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public Line toEntity() {
        return new Line(this.name, this.color, this.upStationId, this.downStationId, this.distance);
    }

    public CreateLineRequest() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
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
