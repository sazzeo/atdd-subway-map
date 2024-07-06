package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.payload.CreateLineRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private static final String URL_PREFIX = "/lines";

    @Nested
    class WhenNew {

        @DisplayName("노선 등록시 노선명,노선색,상행역,하행역,거리를 함께 등록하고 생성된 결과를 응답받는다.")
        @Test
        void createLine() {
            // Given 노선을 생성하면
            // Then 신규 노선이 생성된다.
            var response = LineAcceptanceTest.this.createLine("2호선", "bg-green-600", 1L, 2L, 10L);

            //Then 생성된 노선을 응답받는다.
            assertAll(() -> {
                var jsonPath = response.jsonPath();
                assertThat(jsonPath.getString("name")).isEqualTo("2호선");
                assertThat(jsonPath.getString("color")).isEqualTo("bg-green-600");
                assertThat(jsonPath.getList("stations.id", Long.TYPE)).containsAnyOf(1L, 2L);
            });

        }

    }

    @Nested
    class WhenShow {

        @DisplayName("모든 노선과 해당하는 지하철역을 모두 반환한다.")
        @Test
        void showLines() {
            //Given 노선을 생성하고
            createLine("2호선", "bg-green-600", 1L, 2L, 10L);
            createLine("3호선", "bg-orange-600", 3L, 4L, 20L);

            //When 노선 목록을 조회하면
            var jsonPath = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .basePath(URL_PREFIX)
                    .when().get()
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract().response().jsonPath();

            //Then 생성된 노선 목록이 모두 조회된다.
            assertAll(() -> {
                assertThat(jsonPath.getList("name")).containsAnyOf("2호선", "3호선");
                assertThat(jsonPath.getList("color")).containsAnyOf("bg-green-600", "bg-orange-600");
                assertThat(jsonPath.getList("stations[0].id", Long.TYPE)).containsAnyOf(1L, 2L);
                assertThat(jsonPath.getList("stations[1].id", Long.TYPE)).containsAnyOf(3L, 4L);
            });

        }

        @DisplayName("특정 노선에 해당하는 정보와 역을 반환한다.")
        @Test
        void showLine() {
            //Given 노선을 여러개 생성하고
            var response = createLine("2호선", "bg-green-600", 1L, 2L, 10L);
            createLine("3호선", "bg-orange-600", 4L, 5L, 10L);
            createLine("4호선", "bg-blue-600", 5L, 6L, 10L);

            //When 한 노선을 조회하면
            var location = response.header(HttpHeaders.LOCATION);

            var jsonPath = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().get(location)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract().response().jsonPath();

            // Then 해당 노선이 조회된다.

            assertAll(() -> {
                assertThat(jsonPath.getString("name")).isEqualTo("2호선");
                assertThat(jsonPath.getString("color")).isEqualTo("bg-green-600");
                assertThat(jsonPath.getList("stations.id", Long.TYPE)).containsAnyOf(1L, 2L);
            });

        }

    }


    private Response createLine(final String name, final String color, final Long upStationId, final Long downStationId, final Long distance) {
        var extractableResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .basePath(URL_PREFIX)
                .body(new CreateLineRequest(name, color, upStationId, downStationId, distance))
                .when().post()
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return extractableResponse.response();
    }

}
