package com.projeto_integrado_biblioteca.domains.session;

import com.projeto_integrado_biblioteca.domains.session.dto.SessionStartRequest;
import com.projeto_integrado_biblioteca.domains.session.dto.LastReadBookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;


    @PostMapping
    public ResponseEntity<Void> startSession(@RequestBody SessionStartRequest request) {

        sessionService.startSession(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LastReadBookResponse> getUserLastReadBook(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(sessionService.getUserLastReadBook(userId));
    }

    @PatchMapping("/{sessionBookId}")
    public ResponseEntity<Void> updateProgress(@PathVariable Long sessionBookId, @RequestParam Integer currentPage){
        sessionService.updateProgress(sessionBookId, currentPage);
        return ResponseEntity.noContent().build();
    }
}
