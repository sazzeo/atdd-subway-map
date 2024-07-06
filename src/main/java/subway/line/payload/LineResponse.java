package subway.line.payload;

public class LineResponse {

  private String name;
  private String color;
  private Long upStationId;
  private Long downStationId;
  private Long distance;

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
