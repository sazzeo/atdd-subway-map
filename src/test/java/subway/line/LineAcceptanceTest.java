package subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.station.StationApiRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LineAcceptanceTest {

    private Long 강남역Id;
    private Long 선릉역Id;
    private Long 삼성역Id;

    @BeforeEach
    void setUp() {
        강남역Id = StationApiRequest.create("강남역").jsonPath().getLong("id");
        선릉역Id = StationApiRequest.create("선릉역").jsonPath().getLong("id");
        삼성역Id = StationApiRequest.create("삼성역").jsonPath().getLong("id");
    }

    @Nested
    class WhenNew {

        @DisplayName("노선 등록시 노선명,노선색,상행역,하행역,거리를 함께 등록하고 생성된 결과를 응답받는다.")
        @Test
        void createLine() {
            // Given 노선을 생성하면

            // Then 신규 노선이 생성된다.
            var response = LineApiRequest.create("2호선", "bg-green-600", 강남역Id, 선릉역Id, 10L);

            //Then 생성된 노선을 응답받는다.
            assertAll(() -> {
                var jsonPath = response.jsonPath();
                assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                assertThat(jsonPath.getString("name")).isEqualTo("2호선");
                assertThat(jsonPath.getString("color")).isEqualTo("bg-green-600");
                assertThat(jsonPath.getList("stations.name", String.class)).containsAnyOf("강남역", "선릉역");
            });

        }

    }

    @Nested
    class WhenShow {

        @DisplayName("모든 노선과 해당하는 지하철역을 모두 반환한다.")
        @Test
        void showLines() {

            //Given 노선을 생성하고
            LineApiRequest.create("2호선", "bg-green-600", 강남역Id, 선릉역Id, 10L);
            LineApiRequest.create("3호선", "bg-orange-600", 선릉역Id, 삼성역Id, 20L);

            //When 노선 목록을 조회하면
            var jsonPath = LineApiRequest.getLines().jsonPath();

            //Then 생성된 노선 목록이 모두 조회된다.
            assertAll(() -> {
                assertThat(jsonPath.getList("name")).containsAnyOf("2호선", "3호선");
                assertThat(jsonPath.getList("color")).containsAnyOf("bg-green-600", "bg-orange-600");
                assertThat(jsonPath.getList("stations[0].name", String.class)).containsAnyOf("강남역" , "선릉역");
                assertThat(jsonPath.getList("stations[1].name", String.class)).containsAnyOf("선릉역" , "삼성역");
            });

        }

        @DisplayName("특정 노선에 해당하는 정보와 역을 반환한다.")
        @Test
        void showLine() {
            //Given 노선을 여러개 생성하고
            var response = LineApiRequest.create("2호선", "bg-green-600", 1L, 2L, 10L);
            LineApiRequest.create("3호선", "bg-orange-600", 4L, 5L, 10L);
            LineApiRequest.create("4호선", "bg-blue-600", 5L, 6L, 10L);

            //When 한 노선을 조회하면
            var location = response.header(HttpHeaders.LOCATION);
            var jsonPath = LineApiRequest.getLine(location).jsonPath();

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
            var response = LineApiRequest.create("2호선", "bg-green-600", 1L, 2L, 10L);
            var location = response.header(HttpHeaders.LOCATION);

            //When 노선을 수정한 뒤
            LineApiRequest.update(location, "다른호선", "red");

            //When 조회하면

            var jsonPath = LineApiRequest.getLine(location).jsonPath();

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
            var response = LineApiRequest.create("2호선", "bg-green-600", 1L, 2L, 10L);
            var location = response.header(HttpHeaders.LOCATION);

            //When 노선을 삭제하면
            LineApiRequest.delete(location);

            //TODO
            //Then 목록에서 제외된다
        }

        @DisplayName("삭제하려는 노선이 존재하지 않으면 응답코드 400을 반환한다")
        @Test
        void deleteLineWhenNotExist() {
            //When 존재하지 않는 노선을 삭제하면
            var response = LineApiRequest.delete("/lines/0");

            //Then 400 를 발생시킨다
            assertThat(response.statusCode()).isEqualTo(400);
        }


    }


}
