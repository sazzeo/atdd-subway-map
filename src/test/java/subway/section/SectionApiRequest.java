package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import subway.line.payload.AddSectionRequest;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionApiRequest {

    public static Response 구간을_추가한다(final Long 노선, final AddSectionRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(String.format("/lines/%d/sections", 노선))
                .then().log().all()
                .extract().response();
    }

}
