package org.example.adapters.out.api;
import com.google.gson.Gson;
import org.example.domain.models.ExchangeRatesResponse;
import org.example.domain.ports.out.IExchangeRateProvider;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class ExchangeRateApiAdapter implements IExchangeRateProvider {
    private final String API_KEY;
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private final HttpClient httpClient;
    private final Gson gson;
    public ExchangeRateApiAdapter() {
        this.API_KEY = loadApiKey();
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }
    private String loadApiKey() {
        Properties props = new Properties();
        String rutaArchivo = "/.properties";
        try (InputStream input = getClass().getResourceAsStream(rutaArchivo)) {
            if (input == null) {
                throw new IOException("No se pudo encontrar 'config.properties' en src/main/resources");
            }
            props.load(input);
            String key = props.getProperty("API_KEY");
            if (key == null || key.trim().isEmpty()) {
                throw new RuntimeException("API_KEY no fue encontrada dentro de config.properties");
            }
            return key;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error fatal: No se pudo leer el archivo 'config.properties'.\n" +
                            "Asegúrate de que el archivo exista en 'src/main/resources/' y contenga tu API_KEY.\n" +
                            "La aplicación se cerrará.\nError: " + e.getMessage(),
                    "Error de Configuración",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return null;
        }
    }

    @Override
    public ExchangeRatesResponse getRates(String baseCurrency) {
        String url = API_BASE_URL + API_KEY + "/latest/" + baseCurrency;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Error en la API: " + response.statusCode() + " - " + response.body());
            }
            String jsonBody = response.body();
            ExchangeRatesResponse ratesResponse = gson.fromJson(jsonBody, ExchangeRatesResponse.class);
            if (!"success".equals(ratesResponse.result())) {
                throw new RuntimeException("La API devolvió un error: " + jsonBody);
            }
            return ratesResponse;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("No se pudo conectar a la API de cambio: " + e.getMessage(), e);
        }
    }
}
