package com.github.lucianomota.mscards.application;

import com.github.lucianomota.mscards.domain.ClienteCartao;
import com.github.lucianomota.mscards.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository repository;

    public List<ClienteCartao> listCartoesByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
