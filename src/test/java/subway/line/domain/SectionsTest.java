package subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionsTest {

    private Long 역1;
    private Long 역2;
    private Long 역3;
    private Long 역4;

    @BeforeEach
    void setUp() {
        역1 = 1L;
        역2 = 2L;
        역3 = 3L;
        역4 = 4L;
    }

    @DisplayName("모든 구간에 해당하는 역 Id을 차례대로 반환한다")
    @Test
    void getLastStationIdTest() {
        //Given 역 4개가 있을때
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));
        sections.add(new Section(역2, 역3, 10L));
        sections.add(new Section(역3, 역4, 10L));

        //when 모든 구간을 조회하면
        var sectionIds = sections.getAllStationIds();

        // Then 역Id를 등록한 순서대로 반환한다
        assertThat(sectionIds).containsExactly(1L, 2L, 3L, 4L);
    }


    @DisplayName("새로 등록하려는 역이 기존 하행역이 아닌 경우 에러를 발생시킨다")
    @Test
    void addTest1() {
        //Given 기존 노선에
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));

        //When 새로 등록하려는 역이 기존 하행역이 아닌 경우
        //Then 에러를 발생시킨다
        assertThrows(InvalidUpStationException.class, () ->
                sections.add(new Section(역1, 역3, 20L))
        );
    }

    @DisplayName("하행역으로 등록하려는 역이 이미 존재하는 경우 에러를 발생시킨다")
    @Test
    void addTest2() {
        //Given 기존 노선에
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));

        //When 하행역으로 등록하려는 역이 이미 존재하는 경우
        //Then 에러를 발생시킨다
        assertThrows(InvalidDownStationException.class, () ->
                sections.add(new Section(역2, 역1, 20L))
        );
    }

    ///////////////////////////////////////////////////////////
    @DisplayName("기존 구간에 추가하려는 상행종점역이 존재하지 않으면 에러를 발생시킨다")
    @Test
    void addTest3() {
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));
        sections.add(new Section(역2, 역3, 10L));
        sections.add(new Section(역3, 역4, 10L));

        var 새구간상행역 = 5L;
        var 새구간하행역 = 6L;
        assertThrows(LineHasNoStationException.class, () -> {
            sections.add(new Section(새구간상행역, 새구간하행역, 10L));
        });
    }



    @DisplayName("기존 구간 가운데에 역을 추가할 수 있다")
    @Test
    void addTest4() {
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));
        sections.add(new Section(역2, 역3, 10L));
        sections.add(new Section(역3, 역4, 10L));


        var 새역 = 5L;

        sections.add(new Section(역2, 새역, 10L));


    }

    ///////////////////////////////////////////////////////////

    @DisplayName("기존 하행역을 상행역으로 하는 구간을 추가한다")
    @Test
    void addSuccessTest() {
        //Given
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));

        //When 기존 하행역을 상행역으로 하는 구간을 추가시
        sections.add(new Section(역2, 역3, 10L));

        //Then 다시 조회했을때 추가된 역을 확인 할 수 있다
        assertThat(sections.getAllStationIds()).containsExactly(역1, 역2, 역3);
    }


    @DisplayName("삭제 하려는 역이 종착역이 아닌 경우 에러를 발생시킨다")
    @Test
    void removeTest1() {
        //Given 기존 노선에
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));

        //When 삭제 하려는 역이 종착역이 아닌 경우
        //Then 에러를 발생시킨다
        assertThrows(NotTerminusStationException.class, () ->
                sections.removeLastStation(1L)
        );
    }


    @DisplayName("구간이 1개밖에 없는 경우 종착역 삭제시 에러를 발생시킨다")
    @Test
    void removeTest2() {
        //Given 구간이 1개밖에 없는 경우
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));

        //When  종착역 삭제시
        //Then 에러를 발생시킨다
        assertThrows(InsufficientStationsException.class, () ->
                sections.removeLastStation(2L)
        );
    }


    @DisplayName("구간이 2개 이상일 떄는 종착역 삭제에 성공한다")
    @Test
    void removeSuccessTest() {
        //Given 구간이 2개 이상일 때
        var sections = new Sections();
        sections.add(new Section(역1, 역2, 10L));
        sections.add(new Section(역2, 역3, 10L));

        sections.removeLastStation(역3);
        assertThat(sections.getAllStationIds()).containsExactly(역1, 역2);

    }


}
