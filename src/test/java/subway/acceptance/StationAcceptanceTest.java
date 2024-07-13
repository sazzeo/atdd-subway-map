package subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // When  지하철역을 생성하면
        var response = StationApiRequest.create("강남역");
        // Then 지하철역이 생성된다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        List<String> stationNames = getStationNames();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    @DisplayName("지하철역을 모두 조회한다.")
    @Test
    void showStations() {
        // Given 2개의 지하철역을 생성하고
        StationApiRequest.create("강남역");
        StationApiRequest.create("선릉역");

        // When 지하철역 목록을 조회하면
        List<String> stationNames = this.getStationNames();

        // Then 2개의 지하철역을 응답 받는다
        assertThat(stationNames).containsOnly("강남역", "선릉역");
    }


    @DisplayName("지하철역을 삭제한다")
    @Test
    void deleteStation() {
        // Given 지하철역을 2개를 생성하고
        String location = StationApiRequest.create("강남역").header("location");
        StationApiRequest.create("선릉역");

        // When 그중 하나의 지하철역을 삭제하면
        RestAssured.given().log().all()
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Then 나머지 1개의 지하철역만 응답받는다
        List<String> stationNames = this.getStationNames();

        assertThat(stationNames).containsOnly("선릉역");
    }

    private List<String> getStationNames() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList("name", String.class);
    }
}
