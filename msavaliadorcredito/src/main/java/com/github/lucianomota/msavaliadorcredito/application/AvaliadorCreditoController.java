package com.github.lucianomota.msavaliadorcredito.application;

import com.github.lucianomota.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import com.github.lucianomota.msavaliadorcredito.application.exception.ErroComunicacaoMicroserviceException;
import com.github.lucianomota.msavaliadorcredito.domain.model.DadosAvaliacao;
import com.github.lucianomota.msavaliadorcredito.domain.model.RetornoAvalicaoCliente;
import com.github.lucianomota.msavaliadorcredito.domain.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadoCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroserviceException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
        try {
            RetornoAvalicaoCliente retornoAvalicaoCliente = avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvalicaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicroserviceException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }
}
