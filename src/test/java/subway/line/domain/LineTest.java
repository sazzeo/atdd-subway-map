package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.InvalidUpStationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LineTest {

    @DisplayName("새로 등록하려는 상행역이 기존 하행역이 아니면 에러를 발생시킨다")
    @Test
    void test1() {
        //given 기존역에
        var 상행역 = 1L;
        var 하행역 = 2L;
        Line line = new Line("2호선", "green", new Section(상행역, 하행역, 10L));

        //when 새로 등록하려는 상행역이 기존 하행역이 아니면
        var 새상행역 = 3L;
        var 새하행역 = 4L;
        //then 에러를 발생시킨다
        assertThrows(InvalidUpStationException.class, () -> {
            line.addSection(새상행역, 새하행역, 20L);
        });

    }


}
