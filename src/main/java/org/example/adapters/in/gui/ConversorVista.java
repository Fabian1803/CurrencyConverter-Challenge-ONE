package org.example.adapters.in.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConversorVista {

    private JFrame ventana;
    private JTextField campoMonto;
    private JComboBox<String> comboDe;
    private JComboBox<String> comboA;
    private JButton botonConvertir;
    private JLabel valorResultado;

    public void iniciar() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el look and feel del sistema.");
        }

        // CREAR LA VENTANA
        ventana = new JFrame("FxConverter - Conversor de Moneda");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(400, 250);
        ventana.setLocationRelativeTo(null); // Centrar

        // CREAR EL PANEL
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // CREAR LOS COMPONENTES
        JLabel etiquetaMonto = new JLabel("Monto a convertir:");
        campoMonto = new JTextField(); // Campo para escribir

        JLabel etiquetaDe = new JLabel("De:");
        String[] monedas = {"USD", "PEN", "EUR", "JPY", "MXN", "BRL"};
        comboDe = new JComboBox<>(monedas);

        JLabel etiquetaA = new JLabel("A:");
        comboA = new JComboBox<>(monedas);

        botonConvertir = new JButton("Convertir");

        JLabel etiquetaResultado = new JLabel("Resultado:");
        valorResultado = new JLabel("0.00");
        valorResultado.setFont(new Font("Arial", Font.BOLD, 16));

        // AÑADIR COMPONENTES AL PANEL
        panel.add(etiquetaMonto);
        panel.add(campoMonto);
        panel.add(etiquetaDe);
        panel.add(comboDe);
        panel.add(etiquetaA);
        panel.add(comboA);
        panel.add(new JLabel()); // Espacio vacío
        panel.add(botonConvertir);
        panel.add(etiquetaResultado);
        panel.add(valorResultado);

        // AÑADIR EL "ACTION LISTENER" (La lógica del botón)
        botonConvertir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cuando el usuario haga clic, llamamos a un método privado
                // Esto es más limpio que poner toda la lógica aquí
                realizarConversion();
            }
        });

        // AÑADIR EL PANEL A LA VENTANA Y MOSTRARLA
        ventana.add(panel);
        ventana.setVisible(true); // ¡Hacerla visible!
    }

    private void realizarConversion() {
        // --- Aquí irá la lógica de conversión ---
        // Por ahora, solo mostraremos un mensaje

        try {
            // 1. Obtener los datos de la UI
            String montoTexto = campoMonto.getText();
            double monto = Double.parseDouble(montoTexto);
            String monedaDe = (String) comboDe.getSelectedItem();
            String monedaA = (String) comboA.getSelectedItem();

            // 2. Simular el cálculo (¡AQUÍ LLAMAREMOS A LA API!)
            // (Tasa inventada por ahora)
            double tasaSimulada = 3.85;
            double resultado = monto * tasaSimulada;

            // 3. Mostrar el resultado
            valorResultado.setText(String.format("%.2f %s", resultado, monedaA));

        } catch (NumberFormatException ex) {
            // Si el usuario escribe "hola" en el monto
            JOptionPane.showMessageDialog(ventana, "Error: El monto debe ser un número.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Cualquier otro error
            JOptionPane.showMessageDialog(ventana, "Ocurrió un error inesperado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
