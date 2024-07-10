package subway.section;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineApiRequest;
import subway.station.StationApiRequest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private Long 최초상행종점역;
    private Long 최초하행종점역;
    private Long 삼성역;
    private Long 잠실역;

    private Long 수인분당선;

    @BeforeEach
    void setUp() {
        최초상행종점역 = StationApiRequest.create("최초상행종점역").jsonPath().getLong("id");
        최초하행종점역 = StationApiRequest.create("최초하행종점역").jsonPath().getLong("id");
        삼성역 = StationApiRequest.create("삼성역").jsonPath().getLong("id");
        잠실역 = StationApiRequest.create("잠실역").jsonPath().getLong("id");

        수인분당선 = LineApiRequest.create("수인분당선", "yellow", 최초상행종점역, 최초하행종점역, 10L).jsonPath().getLong("id");
    }

    @Nested
    class WhenAddSection {

        @DisplayName("기존 하행종점역이 새로운 구간의 하행종점역이 아니면 새 구간 등록시 400 상태코드를 반환한다.")
        @Test
        void test1() {
            //given 기존 하행종점역이 새로운 구간의 상행 종점역이 아니면
            var request = (Map.of("downStationId", 삼성역,
                    "upStationId", 잠실역,
                    "distance", 0));

            //when 새 구간 등록시
            var response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post(String.format("/lines/%d/sections", 수인분당선))
                    .then().log().all()
                    .extract().response();

            //then 400 상태코드를 반환한다
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("이미 노선에 등록되어있는 역을 하행종점역으로 등록하면 400 상태코드를 반환한다.")
        @Test
        void test2() {
            //given 이미 노선에 등록되어있는 역을 하행종점역으로 등록하면
            var request = (Map.of("downStationId", 최초하행종점역,
                    "upStationId", 최초상행종점역,
                    "distance", 10));

            //when 새 구간 등록시
            var response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post(String.format("/lines/%d/sections", 수인분당선))
                    .then().log().all()
                    .extract().response();

            //then 400 상태코드를 반환한다
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }


        @DisplayName("새로운 구간 등록후 해당 노선을 조회하면 등록된 모든 역을 확인 할 수 있다.")
        @Test
        void test3() {
            //given 새로운 구간 등록에 성공하면
            var request = (Map.of("downStationId", 최초하행종점역,
                    "upStationId", 삼성역,
                    "distance", 10));

            var createdResponse = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().post(String.format("/lines/%d/sections", 수인분당선))
                    .then().log().all()
                    .extract().response();

            //when 노선 조회시
            var jsonPath = LineApiRequest.getLine(String.format("/lines/%d", 수인분당선)).jsonPath();

//            then 등록된 모든 역을 확인할 수 있다.
            assertAll(() -> {
                        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                        assertThat(jsonPath.getList("stations.name", String.class)).containsAnyOf("최초상행종점역", "최초하행종점역", "삼성역");
                    }
            );

        }


    }

    @Nested
    class WhenDeleteSection {
        @DisplayName("제거하려는 역이 선택된 노선의 하행종점역이 아니면 400 상태코드를 반환한다.")
        void test1() {

        }

        @DisplayName("역이 2개 이하로 존재하는 경우 400 상태코드를 반환한다.")
        void test2() {

        }

        @DisplayName(" 역 삭제에 성공후 역을 조회하면 삭제된 역이 조회되지 않는다.")
        void test3() {

        }


    }


}
