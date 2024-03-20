package io.github.lucianomota.msclientes.application;

import io.github.lucianomota.msclientes.domain.Client;
import io.github.lucianomota.msclientes.infra.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository reepository;

    @Transactional
    public Client save(Client client) {
        return reepository.save(client);
    }

    public Optional<Client> getByCpf(String cpf) {
        return reepository.findByCpf(cpf);
    }
}
