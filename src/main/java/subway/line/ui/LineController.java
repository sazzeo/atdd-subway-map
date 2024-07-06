package subway.line.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;

@RequestMapping("/lines")
@RestController
public class LineController {

  @PostMapping
  public ResponseEntity<LineResponse> createLine(@RequestBody CreateLineRequest request) {
    return ResponseEntity.created(URI.create("/lines/1")).body(null);
  }

}
