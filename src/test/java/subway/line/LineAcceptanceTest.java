package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

  private static final String URL_PREFIX = "/lines";

  @Nested
  class WhenNew {

    @DisplayName("노선 등록시 노선명,노선색,상행역,하행역,거리를 함께 등록한다")
    @Test
    void createLine() {
      // Given 노선을 생성하면
      var lineFixture = LineFixture.create();

      // Then 신규 노선이 생성된다.
      var createdExtractableResponse = RestAssured.given().log().all()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .basePath(URL_PREFIX)
          .body(lineFixture)
          .when().post()
          .then().log().all()
          .statusCode(HttpStatus.CREATED.value())
          .extract();

      assertThat(createdExtractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

//      // Then 신규 생성된 노선이 조회된다.
//      var createdUrl = createdExtractableResponse.header("location");
//      var lineJsonPath = RestAssured.given().log().all()
//          .contentType(MediaType.APPLICATION_JSON_VALUE)
//          .body(params)
//          .when().get(createdUrl)
//          .then().log().all()
//          .statusCode(HttpStatus.OK.value())
//          .extract().response().jsonPath();
//
//      var name = lineJsonPath.getString("name");
//      var color = lineJsonPath.getString("color");
//      List<Long> stations = lineJsonPath.getList("$.stations[*].id");
//
//      assertAll(() -> {
//            assertThat(name).isEqualTo("2호선");
//            assertThat(color).isEqualTo("bg-green-600");
//            assertThat(stations).containsAnyOf(1L, 2L);
//          }
//      );
    }

  }
}
