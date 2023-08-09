package com.github.lucianomota.msavaliadorcredito.application;

import com.github.lucianomota.msavaliadorcredito.application.exception.DadosClienteNotFoundException;
import com.github.lucianomota.msavaliadorcredito.application.exception.ErroComunicacaoMicroserviceException;
import com.github.lucianomota.msavaliadorcredito.application.exception.ErroSolicitacaoCartaoException;
import com.github.lucianomota.msavaliadorcredito.domain.model.CartaoAprovado;
import com.github.lucianomota.msavaliadorcredito.domain.model.CartaoCliente;
import com.github.lucianomota.msavaliadorcredito.domain.model.DadosCliente;
import com.github.lucianomota.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import com.github.lucianomota.msavaliadorcredito.domain.model.ProtocoloSolicitacaoCartao;
import com.github.lucianomota.msavaliadorcredito.domain.model.RetornoAvalicaoCliente;
import com.github.lucianomota.msavaliadorcredito.domain.model.SituacaoCliente;
import com.github.lucianomota.msavaliadorcredito.infra.clients.CartoesResourceClient;
import com.github.lucianomota.msavaliadorcredito.infra.clients.ClienteResourceClient;
import com.github.lucianomota.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadoCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient clientCartoes;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroserviceException {
        try {
            ResponseEntity<List<CartaoCliente>> cartoesResponse = clientCartoes.getCartoesByCliente(cpf);
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);

            return SituacaoCliente.builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(e.getMessage(), status);
        }
    }

    public RetornoAvalicaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroserviceException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = clientCartoes.getCartoesRendaAteh(renda);

            List<CartaoCliente> cartoes = cartoesResponse.getBody();

            var listCartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteLiberado();
                BigDecimal remdaBD = BigDecimal.valueOf(renda);
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());

                BigDecimal fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvalicaoCliente(listCartoesAprovados);

        } catch (FeignException.FeignClientException e) {
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroserviceException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {
        try {
            emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
