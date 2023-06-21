package com.github.lucianomota.mscards.application.representation;

import com.github.lucianomota.mscards.domain.BandeiraCartao;
import com.github.lucianomota.mscards.domain.Cartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartoesSaveRequest {

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel() {
        return new Cartao(nome, bandeira, renda, limite);
    }
}
