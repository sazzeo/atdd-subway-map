package subway.line.payload;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<LineStationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final List<LineStationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public List<LineStationResponse> getStations() {
        return stations;
    }

}
