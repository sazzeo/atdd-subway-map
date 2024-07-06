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
            var lineRequest = LineFixture.create("2호선", "bg-green-600", 1L, 2L,10L );

            // Then 신규 노선이 생성된다.
            var extractableResponse = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .basePath(URL_PREFIX)
                    .body(lineRequest)
                    .when().post()
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract();

            assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            var jsonPath = extractableResponse.response().jsonPath();

            //Then 생성된 노선을 응답받는다.
            assertAll(() -> {
                assertThat(jsonPath.getString("color")).isEqualTo(lineRequest.getColor());
                assertThat(jsonPath.getString("name")).isEqualTo(lineRequest.getName());
                assertThat(jsonPath.getList("stations.id" , Long.TYPE)).containsAnyOf(lineRequest.getUpStationId(), lineRequest.getDownStationId());
            });

        }



    }
}
