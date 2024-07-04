package subway.line;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private static final String URL_PREFIX = "/lines";

    @Nested
    class WhenNew {

        @Test
        void 노선_등록시_노선명_노선색_상행역_하행역_거리를_함께_등록한다() {

            // Given 노선을 생성하면
            var params = Map.of("name", "2호선",
                    "color", "bg-green-600",
                    "upStationId", 1,
                    "downStationId", 2,
                    "distance", 10);

            // Then 신규 노선이 생성된다.
            var createdExtractableResponse = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .when().post(URL_PREFIX)
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract();

            assertThat(createdExtractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            // Then 신규 생성된 노선이 조회된다.
            var createdUrl = createdExtractableResponse.header("location");
            var lineJsonPath = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(params)
                    .when().get(createdUrl)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract().response().jsonPath();

            var name = lineJsonPath.getString("name");
            var color = lineJsonPath.getString("color");
            List<Long> stations = lineJsonPath.getList("$.stations[*].id");

            assertAll(() -> {
                        assertThat(name).isEqualTo("2호선");
                        assertThat(color).isEqualTo("bg-green-600");
                        assertThat(stations).containsAnyOf(1L,2L);
                    }
            );


        }

    }
}
