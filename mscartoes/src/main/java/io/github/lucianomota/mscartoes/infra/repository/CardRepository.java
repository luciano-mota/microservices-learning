package io.github.lucianomota.mscartoes.infra.repository;

import io.github.lucianomota.mscartoes.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByIncomeLessThanEqual(BigDecimal incomeBigDecimal);
}
