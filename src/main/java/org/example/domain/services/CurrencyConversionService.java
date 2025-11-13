package org.example.domain.services;
import org.example.domain.models.ConversionResult;
import org.example.domain.models.ExchangeRatesResponse;
import org.example.domain.ports.in.ICurrencyConversionService;
import org.example.domain.ports.out.IExchangeRateProvider;
import java.util.Map;
public class CurrencyConversionService implements ICurrencyConversionService {
    private final IExchangeRateProvider rateProvider;

    public CurrencyConversionService(IExchangeRateProvider rateProvider) {
        this.rateProvider = rateProvider;
    }

    @Override
    public ConversionResult convert(String fromCurrency, String toCurrency, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo.");
        }
        ExchangeRatesResponse response = rateProvider.getRates(fromCurrency);
        Map<String, Double> rates = response.conversion_rates();

        if (rates == null || !rates.containsKey(toCurrency)) {
            throw new RuntimeException("Moneda de destino no encontrada: " + toCurrency);
        }
        double exchangeRate = rates.get(toCurrency);
        double convertedAmount = amount * exchangeRate;
        return new ConversionResult(
                fromCurrency,
                toCurrency,
                amount,
                exchangeRate,
                convertedAmount
        );
    }

    @Override
    public String[] getAvailableCurrencies() {
        ExchangeRatesResponse response = rateProvider.getRates("USD");
        java.util.Set<String> currencyKeys = response.conversion_rates().keySet();
        java.util.Set<String> sortedKeys = new java.util.TreeSet<>(currencyKeys);
        return sortedKeys.toArray(new String[0]);
    }
}
