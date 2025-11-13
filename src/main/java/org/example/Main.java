package org.example;
import org.example.adapters.in.gui.ConversorVista;
import org.example.adapters.in.gui.SplashVentana;
import org.example.adapters.out.api.ExchangeRateApiAdapter;
import org.example.domain.ports.in.ICurrencyConversionService;
import org.example.domain.ports.out.IExchangeRateProvider;
import org.example.domain.services.CurrencyConversionService;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Runnable tareaPrincipal = () -> {
                IExchangeRateProvider apiClient = new ExchangeRateApiAdapter();
                ICurrencyConversionService cerebro = new CurrencyConversionService(apiClient);
                ConversorVista vista = new ConversorVista(cerebro);
                vista.iniciar();
                new SwingWorker<String[], Void>() {
                    @Override
                    protected String[] doInBackground() throws Exception {
                        return cerebro.getAvailableCurrencies();
                    }
                    @Override
                    protected void done() {
                        try {
                            String[] monedas = get();
                            vista.setMonedas(monedas);
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null,
                                    "Error fatal: No se pudo cargar la lista de monedas.\n" +
                                            "Revise su conexión a internet y reinicie la aplicación.\n" +
                                            e.getMessage(),
                                    "Error de Red",
                                    JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        }
                    }
                }.execute();
            };
            SplashVentana splash = new SplashVentana(tareaPrincipal);
            splash.setVisible(true);
        });
    }
}