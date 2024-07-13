package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.SectionDistanceNotValidException;
import subway.line.exception.SectionNotValidException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SectionTest {

    @DisplayName("생성시 상행역과 하행역이 같으면 에러를 반환한다")
    @Test
    void createTest() {
        var 삼성역 = 1L;
        assertThrows(SectionNotValidException.class, () -> {
            new Section(삼성역, 삼성역, 10L);
        });
    }

    @DisplayName("거리가 1미만이면 에러를 반환한다")
    @Test
    void distanceTest() {
        var 삼성역 = 1L;
        var 선릉역 = 2L;

        assertThrows(SectionDistanceNotValidException.class, () -> {
            new Section(삼성역, 선릉역, 0L);
        });
    }

}
