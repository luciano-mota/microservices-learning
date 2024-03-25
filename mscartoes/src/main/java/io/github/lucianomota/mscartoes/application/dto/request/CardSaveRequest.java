package io.github.lucianomota.mscartoes.application.dto.request;

import io.github.lucianomota.mscartoes.domain.Card;
import io.github.lucianomota.mscartoes.domain.CardBanner;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardSaveRequest {

    private String name;
    private CardBanner banner;
    private BigDecimal income;
    private BigDecimal limit;

    public Card toModel() {
        return new Card(name, banner, income, limit);
    }
}
