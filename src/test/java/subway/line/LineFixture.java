package subway.line;

import subway.line.payload.CreateLineRequest;

public class LineFixture {

    public static CreateLineRequest create(String name, String color, Long upStationId, Long downStationId, Long distance) {
        return new CreateLineRequest(
                name, color, upStationId, downStationId, distance
        );

    }

}
