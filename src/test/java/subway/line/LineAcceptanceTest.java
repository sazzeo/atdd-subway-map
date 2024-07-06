package subway.line;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
            var lineRequest = LineFixture.create("2호선", "bg-green-600", 1L, 2L, 10L);

            // Then 신규 노선이 생성된다.
            var response = LineAcceptanceTest.this.createLine(lineRequest);

            //Then 생성된 노선을 응답받는다.
            assertAll(() -> {
                var jsonPath = response.jsonPath();
                assertThat(jsonPath.getString("color")).isEqualTo(lineRequest.getColor());
                assertThat(jsonPath.getString("name")).isEqualTo(lineRequest.getName());
                assertThat(jsonPath.getList("stations.id", Long.TYPE)).containsAnyOf(lineRequest.getUpStationId(), lineRequest.getDownStationId());
            });

        }

    }

    @Nested
    class WhenShow {

        @DisplayName("모든 노선과 해당하는 지하철역을 모두 반환한다.")
        @Test
        void showLines() {
            //Given 노선을 생성하고
            var lineRequest1 = LineFixture.create("2호선", "bg-green-600", 1L, 2L, 10L);
            var lineRequest2 = LineFixture.create("3호선", "bg-orange-600", 3L, 4L, 20L);
            createLine(lineRequest1);
            createLine(lineRequest2);

            //When 노선 목록을 조회하면
            var jsonPath = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .basePath(URL_PREFIX)
                    .when().get()
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract().response().jsonPath();

            //Then 생성된 노선 목록이 모두 조회된다.
            assertAll(()-> {
                assertThat(jsonPath.getList("name")).containsAnyOf("2호선" , "3호선");
                assertThat(jsonPath.getList("color")).containsAnyOf("bg-green-600" , "bg-orange-600");
                assertThat(jsonPath.getList("stations[0].id" , Long.TYPE)).containsAnyOf(1L , 2L);
                assertThat(jsonPath.getList("stations[1].id" , Long.TYPE)).containsAnyOf(3L , 4L);
            });

        }



    }


    private Response createLine(final CreateLineRequest lineRequest) {
        var extractableResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .basePath(URL_PREFIX)
                .body(lineRequest)
                .when().post()
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return extractableResponse.response();
    }

}
