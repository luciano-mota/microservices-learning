package com.github.lucianomota.msclient.application.representation;

import com.github.lucianomota.msclient.domain.Cliente;
import lombok.Data;

@Data
public class ClientSaveRequest {

    private String cpf;
    private String nome;
    private Integer idade;

    public Cliente toModel() {
        return new Cliente(cpf, nome, idade);
    }
}
