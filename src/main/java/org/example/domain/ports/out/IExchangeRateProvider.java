package org.example.domain.ports.out;
import org.example.domain.models.ExchangeRatesResponse;
public interface IExchangeRateProvider {
    ExchangeRatesResponse getRates(String baseCurrency);
}
