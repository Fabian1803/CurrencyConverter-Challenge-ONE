package org.example.adapters.in.gui;
import javax.swing.*;
import java.awt.*;
public class SplashVentana extends JWindow {
    private JProgressBar barraProgreso;
    private JLabel etiquetaPorcentaje;
    public SplashVentana(Runnable tareaAlTerminar) {
        setSize(450, 260);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));
        JPanel panelConBorde = new JPanel(new BorderLayout());
        panelConBorde.setBackground(Color.WHITE);
        panelConBorde.setBorder(BorderFactory.createLineBorder(new Color(55, 130, 193, 238), 6));
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(Color.WHITE);
        JLabel etiquetaTitulo = new JLabel("Conversor de Moneda - Challenge ONE");
        etiquetaTitulo.setFont(new Font("Helvetica", Font.BOLD, 19));
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel etiquetaCargando = new JLabel("Cargando componentes...");
        etiquetaCargando.setFont(new Font("Helvetica", Font.PLAIN, 14));
        etiquetaCargando.setAlignmentX(Component.CENTER_ALIGNMENT);
        barraProgreso = new JProgressBar();
        barraProgreso.setIndeterminate(false);
        barraProgreso.setStringPainted(false);
        barraProgreso.setMinimum(0);
        barraProgreso.setMaximum(100);
        barraProgreso.setForeground(new Color(55, 130, 193, 238));
        barraProgreso.setBackground(new Color(230, 230, 230));
        Dimension tamanoBarra = new Dimension(420, 20);
        barraProgreso.setPreferredSize(tamanoBarra);
        barraProgreso.setMaximumSize(tamanoBarra);
        barraProgreso.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaPorcentaje = new JLabel("0%");
        etiquetaPorcentaje.setFont(new Font("Helvetica", Font.BOLD, 14));
        etiquetaPorcentaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContenido.add(Box.createVerticalGlue());
        panelContenido.add(etiquetaTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));
        panelContenido.add(etiquetaCargando);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));
        panelContenido.add(barraProgreso);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 5)));
        panelContenido.add(etiquetaPorcentaje);
        panelContenido.add(Box.createVerticalGlue());
        panelConBorde.add(panelContenido, BorderLayout.CENTER);
        add(panelConBorde);
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    final int progressValue = i;
                    SwingUtilities.invokeLater(() -> {
                        barraProgreso.setValue(progressValue);
                        etiquetaPorcentaje.setText(progressValue + "%");
                    });
                    Thread.sleep(20);
                }
                SwingUtilities.invokeLater(() -> {
                    setVisible(false);
                    dispose();
                    tareaAlTerminar.run();
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}