package subway.line.domain;

import subway.station.Station;

import javax.persistence.*;
import javax.security.sasl.SaslClient;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();

    public void add(final Section section) {
        this.sections.add(section);
    }

    public Station getLastDownStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

}
