package subway.line.domain;

import subway.line.exception.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();


    public void add(final Section section) {
        if (this.sections.isEmpty()) {
            sections.add(section);
            return;
        }

        if (!hasStation(section.getUpStationId())) {
            throw new LineHasNoStationException("추가하려는 상행역이 기존 노선에 존재하지 않습니다.");
        }

        if (isUpStationAlreadyExists(section.getDownStationId())) {
            throw new InvalidDownStationException("하행역으로 등록하려는 역이 이미 존재합니다.");
        }

        Section targetSection = getSectionByUpStationId(section.getUpStationId())
                .orElse(null);
        if (targetSection == null) {
            sections.add(section);
            return;
        }

        if (targetSection.getDistance() <= section.getDistance()) {
            throw new LineDistanceNotValidException("신규 추가하려는 구간이 원래 존재했던 구간의 길이보다 길 수 없습니다.");
        }

        Long newDistance = targetSection.getDistance() - section.getDistance();
        targetSection.updateDownStationAndDistance(section.getDownStationId(), newDistance);

        sections.add(section);
    }


    private boolean hasStation(Long stationId) {
        List<Long> stationIds = getAllStationIds();
        return stationIds.contains(stationId);
    }

    public List<Long> getAllStationIds() {
        Map<Long, Long> upDownPair = sections.stream()
                .collect(Collectors.toMap((Section::getUpStationId), Section::getDownStationId));

        Map<Long, Long> downUpPair = sections.stream()
                .collect(Collectors.toMap((Section::getUpStationId), Section::getDownStationId));

        List<Long> stationIds = new ArrayList<>();

        //List Lin
        for (Long key : upDownPair.keySet()) {
            //상행역 key - 하행역 key = > 첫역
            Set<Long> upKey = upDownPair.keySet();
            upKey.remove(downUpPair.keySet());

            if(stationIds.isEmpty()) {
                stationIds.add(key);
            }

            stationIds.add(key);


        }


//        List<Long> stationIds = sections.stream()
//                .map(Section::getUpStationId)
//                .collect(Collectors.toList());

        stationIds.add(getLastDownStationId());
        return stationIds;
    }


    public boolean isNotLastStation(final Long stationId) {
        return !this.getLastDownStationId().equals(stationId);
    }

    public void removeLastStation(final Long stationId) {
        if (isNotLastStation(stationId)) {
            throw new NotTerminusStationException("삭제하려는 역이 종착역이 아닙니다.");
        }
        if (hasOnlyOneSection()) {
            throw new InsufficientStationsException("구간이 1개밖에 없어 역을 삭제할 수 없습니다.");
        }
        sections.remove(sections.size() - 1);
    }

    public boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

    private Long getLastDownStationId() {
        return sections.get(sections.size() - 1).getDownStationId();
    }

    private Optional<Section> getSectionByUpStationId(final Long stationId) {
        return sections
                .stream()
                .filter(section -> section.getUpStationId().equals(stationId))
                .findFirst();
    }

    private boolean isUpStationAlreadyExists(Long stationId) {
        return sections.stream()
                .map(Section::getUpStationId)
                .anyMatch(id -> id.equals(stationId));
    }
}
