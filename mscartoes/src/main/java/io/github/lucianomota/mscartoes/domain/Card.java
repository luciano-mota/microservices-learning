package io.github.lucianomota.mscartoes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private CardBanner banner;

    private BigDecimal income;
    private BigDecimal basciLimit;

    public Card(String name, CardBanner banner, BigDecimal income, BigDecimal basciLimit) {
        this.name = name;
        this.banner = banner;
        this.income = income;
        this.basciLimit = basciLimit;
    }
}
