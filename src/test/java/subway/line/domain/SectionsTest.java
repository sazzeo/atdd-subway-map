package subway.line.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @DisplayName("모든 구간에 해당하는 역 Id을 차례대로 반환한다")
    @Test
    void getLastStationIdTest() {
        //Given 역 4개가 있을때
        var 역1 = 1L;
        var 역2 = 2L;
        var 역3 = 3L;
        var 역4 = 4L;
        var sections = new Sections();
        sections.add(new Section(역1, 역2,10L));
        sections.add(new Section(역2, 역3,10L));
        sections.add(new Section(역3, 역4,10L));
        //when 모든 구간을 조회하면
        var sectionIds = sections.getStationIds();

        // Then 역Id를 등록한 순서대로 반환한다
        Assertions.assertThat(sectionIds).containsExactly(1L,2L,3L,4L);
    }

}
