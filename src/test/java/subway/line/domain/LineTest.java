package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.InsufficientStationsException;
import subway.line.exception.InvalidDownStationException;
import subway.line.exception.InvalidUpStationException;
import subway.line.exception.NotTerminusStationException;

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

    @DisplayName("새로 등록하려는 하행역이 이미 등록된 경우 에러를 발생시킨다")
    @Test
    void test2() {
        //given 기존역에
        var 상행역 = 1L;
        var 하행역 = 2L;
        Line line = new Line("2호선", "green", new Section(상행역, 하행역, 10L));

        //when 새로 등록하려는 하행역이 이미 등록된 경우
        var 새상행역 = 2L;
        var 새하행역 = 1L;
        //then 에러를 발생시킨다
        assertThrows(InvalidDownStationException.class, () ->
                line.addSection(새상행역, 새하행역, 20L)
        );

    }

    @DisplayName("역이 3개 이상일 때 삭제하려는 역이 기존 종착역이 아닌 경우 에러를 발생시킨다")
    @Test
    void test3() {
        //역이 3개 이상일때
        var 역1 = 1L;
        var 역2 = 2L;
        var 역3 = 3L;

        Line line = new Line("2호선", "green", new Section(역1, 역2, 10L));
        line.addSection(역2, 역3, 10L);

        //then 에러를 발생시킨다
        assertThrows(NotTerminusStationException.class, () ->
                //when 삭제하려는 역이 현재 종착역이 아닌 경우
                line.removeLastStation(역2)
        );
    }

    @DisplayName("역이 2개 이하로 존재하는 경우 하행역 삭제시 에러를 발생시킨다.")
    @Test
    void test4() {
        //역이 2개 이하로 존재하는 경우
        var 역1 = 1L;
        var 역2 = 2L;
        Line line = new Line("2호선", "green", new Section(역1, 역2, 10L));

        //then 에러를 발생시킨다
        assertThrows(InsufficientStationsException.class, () ->
                //when 하행역 삭제시
                line.removeLastStation(역2)
        );
    }


}
