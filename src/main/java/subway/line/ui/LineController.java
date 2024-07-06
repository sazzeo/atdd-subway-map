package subway.line.ui;

import org.springframework.http.ResponseEntity;
`import org.springframework.web.bind.annotation.*;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;
import subway.line.service.LineService;

import java.net.URI;
import java.util.List;

@RequestMapping("/lines")
@RestController
public class LineController {

  private final LineService lineService;

  public LineController(final LineService lineService) {
    this.lineService = lineService;
  }

  @PostMapping
  public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest request) {
    var response = lineService.saveLine(request);
    return ResponseEntity.created(URI.create("/lines/" + response.getId())).body(response);
  }

  @GetMapping
  public ResponseEntity<List<LineResponse>> showLines() {
    return ResponseEntity.ok(lineService.getLines());
  }

}
