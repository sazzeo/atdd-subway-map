package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.UpdateLineRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
            var jsonPath = getLine(location).response().jsonPath();

            // Then 해당 노선이 조회된다.

            assertAll(() -> {
                assertThat(jsonPath.getString("name")).isEqualTo("2호선");
                assertThat(jsonPath.getString("color")).isEqualTo("bg-green-600");
                assertThat(jsonPath.getList("stations.id", Long.TYPE)).containsAnyOf(1L, 2L);
            });

        }


    }


    @Nested
    class WhenUpdate {

        @DisplayName("노선을 수정한후 조회시 수정된 정보가 반환된다.")
        @Test
        void updateLine() {
            //Given 노선을 생성하고
            var response = createLine("2호선", "bg-green-600", 1L, 2L, 10L);
            var location = response.header(HttpHeaders.LOCATION);

            //When 노선을 수정한 뒤
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new UpdateLineRequest("다른호선" , "red"))
                    .when().patch(location)
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .extract();

            //When 조회하면
            var jsonPath = getLine(location).response().jsonPath();

            //Then 수정된 결과가 반환된다.
            assertAll(() -> {
                assertThat(jsonPath.getString("name")).isEqualTo("다른호선");
                assertThat(jsonPath.getString("color")).isEqualTo("red");
            });
        }

    }

    @Nested
    class WhenDelete {

        @DisplayName("삭제하려는 노선이 존재하면 삭제된 뒤 조회되지 않는다.")
        @Test
        void deleteLine() {
            //Given 노선을 생성하고
            var response = createLine("2호선", "bg-green-600", 1L, 2L, 10L);
            var location = response.header(HttpHeaders.LOCATION);

            //When 노선을 삭제한뒤
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete(location)
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .extract();

            //Then 다시 조회하면 노선이 조회되지 않는다.
            getLine(location, HttpStatus.BAD_REQUEST.value());
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

    private ExtractableResponse<Response> getLine(final String location) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> getLine(final String location , int statusCode) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(location)
                .then().log().all()
                .statusCode(statusCode)
                .extract();
    }

}
