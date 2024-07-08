package subway.line.domain;

import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;

    private String color;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Station> stations = new ArrayList<>();

    private Long distance;

    public Line() {
    }

    public Line(final String name, final String color, final Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getDistance() {
        return distance;
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void addStations(final List<Station> stations) {
        this.stations.addAll(stations);
    }
}
