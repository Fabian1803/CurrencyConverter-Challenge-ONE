package org.example.domain.ports.in;
import org.example.domain.models.ConversionResult;
import org.example.domain.models.ExchangeRatesResponse;
public interface ICurrencyConversionService {
    ConversionResult convert(String fromCurrency, String toCurrency, double amount);
    String[] getAvailableCurrencies();
}
