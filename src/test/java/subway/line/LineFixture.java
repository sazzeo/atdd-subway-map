package subway.line;

import java.util.Map;

public class LineFixture {

  public static Map<String, Object> create() {
    return  Map.of("name", "2호선",
        "color", "bg-green-600",
        "upStationId", 1,
        "downStationId", 2,
        "distance", 10);
  }

}
