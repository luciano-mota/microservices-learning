package com.github.lucianomota.msavaliadorcredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class RetornoAvalicaoCliente {

    private List<CartaoAprovado> cartoes;
}
