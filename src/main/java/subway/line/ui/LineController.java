package subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;
import subway.line.service.LineService;

import java.net.URI;

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

}
