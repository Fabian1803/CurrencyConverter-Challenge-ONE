package org.example.domain.models;
public record ConversionResult(
        String fromCurrency,
        String toCurrency,
        double originalAmount,
        double exchangeRate,
        double convertedAmount
) {
    public String getFormattedResult() {
        return String.format("%.2f %s = %.2f %s",
                originalAmount, fromCurrency,
                convertedAmount, toCurrency
        );
    }
    public String getFormattedRate() {
        return String.format("Tasa: 1 %s = %.4f %s",
                fromCurrency, exchangeRate, toCurrency
        );
    }
}