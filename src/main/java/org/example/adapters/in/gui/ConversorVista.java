package org.example.adapters.in.gui;
import org.example.domain.models.ConversionResult;
import org.example.domain.ports.in.ICurrencyConversionService;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
public class ConversorVista {
    private JFrame ventana;
    private JTextField campoMontoDe;
    private JTextField campoMontoA;
    private JComboBox<String> comboDe;
    private JComboBox<String> comboA;
    private JButton botonSwap;
    private JLabel etiquetaError;
    private final ICurrencyConversionService conversionService;
    private Point puntoInicialDrag;
    private Timer debounceTimerDe;
    private Timer debounceTimerA;
    private boolean isUpdating = false;
    public ConversorVista(ICurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }
    public void iniciar() {
        ventana = new JFrame();
        ventana.setUndecorated(true);
        ventana.setSize(450, 260);
        ventana.setLocationRelativeTo(null);
        ventana.setBackground(new Color(255, 255, 255, 0));
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panelPrincipal.add(crearPanelTitulo(), BorderLayout.NORTH);
        panelPrincipal.add(crearPanelContenido(), BorderLayout.CENTER);
        int DELAY = 200;
        debounceTimerDe = new Timer(DELAY, e -> convertir(campoMontoDe, campoMontoA, comboDe, comboA));
        debounceTimerDe.setRepeats(false);
        debounceTimerA = new Timer(DELAY, e -> convertir(campoMontoA, campoMontoDe, comboA, comboDe));
        debounceTimerA.setRepeats(false);
        DocumentListener listenerDe = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { restartTimer(debounceTimerDe); }
            public void removeUpdate(DocumentEvent e) { restartTimer(debounceTimerDe); }
            public void changedUpdate(DocumentEvent e) { restartTimer(debounceTimerDe); }
        };
        campoMontoDe.getDocument().addDocumentListener(listenerDe);
        DocumentListener listenerA = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { restartTimer(debounceTimerA); }
            public void removeUpdate(DocumentEvent e) { restartTimer(debounceTimerA); }
            public void changedUpdate(DocumentEvent e) { restartTimer(debounceTimerA); }
        };
        campoMontoA.getDocument().addDocumentListener(listenerA);
        comboDe.addActionListener(e -> restartTimer(debounceTimerDe));
        comboA.addActionListener(e -> restartTimer(debounceTimerDe));
        botonSwap.addActionListener(e -> {
            int indexDe = comboDe.getSelectedIndex();
            int indexA = comboA.getSelectedIndex();
            comboDe.setSelectedIndex(indexA);
            comboA.setSelectedIndex(indexDe);
            String montoDe = campoMontoDe.getText();
            isUpdating = true;
            campoMontoA.setText(montoDe);
            isUpdating = false;
            restartTimer(debounceTimerA);
        });
        ventana.add(panelPrincipal);
        ventana.setVisible(true);
    }
    private JPanel crearPanelTitulo() {
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(new Color(55, 130, 193, 238));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);
        ImageIcon iconGithub = cargarIconoApp("/icons/github.png");
        JButton botonGithub = new JButton();
        if (iconGithub != null) {
            botonGithub.setIcon(iconGithub);
        } else {
            botonGithub.setText("G"); // Fallback si no encuentra la imagen
        }
        configurarBotonIcono(botonGithub);
        botonGithub.addActionListener(e -> abrirUrl("https://github.com/Fabian1803"));
        ImageIcon iconClose = cargarIconoApp("/icons/close.png");
        JButton botonCerrar = new JButton();
        if (iconClose != null) {
            botonCerrar.setIcon(iconClose);
        } else {
            botonCerrar.setText("X");
        }
        configurarBotonIcono(botonCerrar);
        botonCerrar.addActionListener(e -> System.exit(0));
        panelBotones.add(botonGithub);
        panelBotones.add(botonCerrar);
        panelTitulo.add(panelBotones, BorderLayout.EAST);
        MouseAdapter ma = new MouseAdapter() {
            public void mousePressed(MouseEvent e) { puntoInicialDrag = e.getPoint(); }
            public void mouseDragged(MouseEvent e) {
                int x = ventana.getLocation().x + e.getX() - puntoInicialDrag.x;
                int y = ventana.getLocation().y + e.getY() - puntoInicialDrag.y;
                ventana.setLocation(x, y);
            }
        };
        panelTitulo.addMouseListener(ma);
        panelTitulo.addMouseMotionListener(ma);
        panelBotones.addMouseListener(ma);
        panelBotones.addMouseMotionListener(ma);
        return panelTitulo;
    }
    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.WEST;
        JLabel titulo = new JLabel();
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.BLACK);
        titulo.setText("<html>Conversor de Moneda " +
                "<span style='font-size: 14pt; font-weight: normal; color: #555555;'>" +
                "- Challenge ONE</span></html>");
        panel.add(titulo, c);
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        JLabel contexto = new JLabel("<html>Utiliza el tipo de cambio real. Datos de API actualizados en vivo.</html>");
        contexto.setFont(new Font("Arial", Font.PLAIN, 12));
        contexto.setForeground(Color.GRAY);
        panel.add(contexto, c);
        c.gridy = 2;
        c.insets = new Insets(15, 0, 15, 0);
        panel.add(new JSeparator(), c);
        c.insets = new Insets(5, 5, 5, 5);
        String[] monedas = {"USD", "PEN", "EUR", "JPY", "MXN", "BRL", "COP", "ARS"};
        MonedaRenderer renderer = new MonedaRenderer();
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 1;
        c.weightx = 0.45;
        comboDe = new JComboBox<>(monedas);
        comboDe.setRenderer(renderer);
        panel.add(comboDe, c);
        c.gridy = 4;
        campoMontoDe = new JTextField("1.00");
        campoMontoDe.setFont(new Font("Arial", Font.BOLD, 28));
        campoMontoDe.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(campoMontoDe, c);
        c.gridx = 1;
        c.gridy = 3;
        c.gridheight = 2;
        c.weightx = 0.1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        botonSwap = new JButton("⇄");
        botonSwap.setFont(new Font("Arial", Font.BOLD, 20));
        botonSwap.setFocusable(false);
        panel.add(botonSwap, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 0.45;
        comboA = new JComboBox<>(monedas);
        comboA.setSelectedIndex(1);
        comboA.setRenderer(renderer);
        panel.add(comboA, c);
        c.gridy = 4;
        campoMontoA = new JTextField();
        campoMontoA.setFont(new Font("Arial", Font.BOLD, 28));
        campoMontoA.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(campoMontoA, c);
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(20, 5, 5, 5);
        JLabel footerTexto = new JLabel("Creado con Java Swing y Arquitectura Hexagonal. Challenge ONE.");
        footerTexto.setFont(new Font("Arial", Font.PLAIN, 10));
        footerTexto.setForeground(Color.GRAY);
        panel.add(footerTexto, c);
        c.gridy = 6; // Fila 6
        c.insets = new Insets(0, 5, 10, 5);
        etiquetaError = new JLabel("");
        etiquetaError.setFont(new Font("Arial", Font.BOLD, 12));
        etiquetaError.setForeground(Color.RED); // Color rojo para errores
        etiquetaError.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(etiquetaError, c);
        return panel;
    }
    public void setMonedas(String[] monedas) {
        isUpdating = true;
        comboDe.removeAllItems();
        comboA.removeAllItems();
        for (String moneda : monedas) {
            comboDe.addItem(moneda);
            comboA.addItem(moneda);
        }
        comboDe.setSelectedItem("USD");
        comboA.setSelectedItem("PEN");
        comboDe.setEnabled(true);
        comboA.setEnabled(true);
        isUpdating = false;
        restartTimer(debounceTimerDe);
    }
    private void restartTimer(Timer timer) {
        if (isUpdating) return;
        timer.restart();
    }
    private void convertir(JTextField campoOrigen, JTextField campoDestino,
                           JComboBox<String> comboOrigen, JComboBox<String> comboDestino) {
        if (isUpdating) return;
        String montoTexto = campoOrigen.getText().trim();
        if (montoTexto.isBlank() || montoTexto.equals(".")) {
            etiquetaError.setText("Introduce un monto válido.");
            return;
        }
        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                etiquetaError.setText("El monto debe ser positivo.");
                return;
            }
            String monedaDe = (String) comboOrigen.getSelectedItem();
            String monedaA = (String) comboDestino.getSelectedItem();
            if (monedaDe == null || monedaA == null) return;
            ConversionResult resultado = conversionService.convert(monedaDe, monedaA, monto);
            isUpdating = true;
            campoDestino.setText(String.format("%.4f", resultado.convertedAmount()));
            etiquetaError.setText(""); // ¡CAMBIO! Limpiamos el error si todo salió bien
        } catch (NumberFormatException ex) {
            etiquetaError.setText("Error: El monto debe ser un número."); // ¡CAMBIO!
        } catch (Exception ex) {
            etiquetaError.setText("Error de API: " + ex.getMessage()); // ¡CAMBIO!
        } finally {
            isUpdating = false;
        }
    }
    private void abrirUrl(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
    private ImageIcon cargarIconoApp(String ruta) {
        try {
            URL urlImagen = getClass().getResource(ruta);
            if (urlImagen != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlImagen);
                Image img = iconoOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void configurarBotonIcono(JButton boton) {
        boton.setFocusable(false);
        boton.setBorder(null);
        boton.setContentAreaFilled(false);
        boton.setMargin(new Insets(2, 5, 2, 5));
        boton.setOpaque(true);
        boton.setBackground(new Color(55, 130, 193, 238));
    }
}
