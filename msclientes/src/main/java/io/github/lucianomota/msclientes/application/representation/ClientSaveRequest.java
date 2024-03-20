package io.github.lucianomota.msclientes.application.representation;

import io.github.lucianomota.msclientes.domain.Client;
import lombok.Data;

import java.security.PrivateKey;

@Data
public class ClientSaveRequest {


    private String cpf;
    private String name;
    private Integer age;

    public Client toModel() {
        return new Client(cpf, name, age);
    }
}
