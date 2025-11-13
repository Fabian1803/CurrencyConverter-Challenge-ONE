package org.example.domain.models;
import java.util.Map;
public record ExchangeRatesResponse(
        String result,
        String base_code,
        Map<String, Double> conversion_rates
) {
}