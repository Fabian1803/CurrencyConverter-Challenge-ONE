package org.example.adapters.out.api;

import org.example.domain.models.ExchangeRatesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExchangeRateApiAdapterTest {
    private ExchangeRateApiAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ExchangeRateApiAdapter();
    }

    @Test
    void testApiCallShouldReturnData() {
        System.out.println("Iniciando prueba de integración: testApiCallShouldReturnData...");
        System.out.println("Intentando conectar con la API para tasas 'USD'...");
        ExchangeRatesResponse response = adapter.getRates("USD");
        System.out.println("¡Conexión exitosa!");
        Assertions.assertNotNull(response);
        Assertions.assertEquals("success", response.result());
        Assertions.assertEquals("USD", response.base_code());
        Assertions.assertNotNull(response.conversion_rates());
        Assertions.assertFalse(response.conversion_rates().isEmpty());
        Assertions.assertTrue(response.conversion_rates().containsKey("EUR"));
        Assertions.assertTrue(response.conversion_rates().containsKey("PEN"));
        System.out.println("\n--- Tasas de ejemplo ---");
        System.out.println("Tasa EUR: " + response.conversion_rates().get("EUR"));
        System.out.println("Tasa PEN: " + response.conversion_rates().get("PEN"));
        System.out.println("\n--- ¡Total de monedas recibidas! ---");
        System.out.println("Total: " + response.conversion_rates().size() + " monedas.");
        System.out.println("\n--- Objeto de Respuesta Completo (toString()) ---");
        System.out.println(response);
        System.out.println("\nPrueba de integración PASÓ con éxito.");
    }
}