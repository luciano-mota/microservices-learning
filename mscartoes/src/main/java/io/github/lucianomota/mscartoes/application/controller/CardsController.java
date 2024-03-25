package io.github.lucianomota.mscartoes.application.controller;

import io.github.lucianomota.mscartoes.application.dto.request.CardSaveRequest;
import io.github.lucianomota.mscartoes.application.service.CardService;
import io.github.lucianomota.mscartoes.domain.Card;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardsController {

    private final CardService service;

    @GetMapping
    public String status(){
        return "OK";
    }

    @PostMapping
    public ResponseEntity insert(@RequestBody CardSaveRequest request) {

        Card card = request.toModel();
        service.save(card);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
