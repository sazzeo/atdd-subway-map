package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = this.createStation("강남역");
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getStationNames();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("지하철역을 모두 조회한다.")
    @Test
    void showStations() {
        // Given 2개의 지하철역을 생성하고
        this.createStation("강남역");
        this.createStation("선릉역");

        // When 지하철역 목록을 조회하면
        List<String> stationNames = this.getStationNames();

        // Then 2개의 지하철역을 응답 받는다
        assertThat(stationNames).containsOnly("강남역", "선릉역");
    }


    @DisplayName("지하철역을 삭제한다")
    @Test
    void deleteStation() {
        // Given 지하철역을 2개를 생성하고
        String location = this.createStation("강남역").header("location");
        this.createStation("선릉역");

        // When 그중 하나의 지하철역을 삭제하면
        RestAssured.given().log().all()
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Then 나머지 1개의 지하철역만 응답받는다
        List<String> stationNames = this.getStationNames();

        assertThat(stationNames).containsOnly("선릉역");
    }

    private ExtractableResponse<Response> createStation(String name) {
        Map<String, String> params = Map.of("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private List<String> getStationNames() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList("name", String.class);
    }
}
