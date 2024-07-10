package subway.station;

public class StationResponse {
    private Long id;
    private String name;

    private StationResponse() {
    }

    private StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(final Station line) {
        return new StationResponse(
                line.getId(),
                line.getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
