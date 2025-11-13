package org.example.adapters.in.gui;
import org.example.adapters.in.utils.MapeoBanderasUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
public class MonedaRenderer extends DefaultListCellRenderer {
    private Map<String, ImageIcon> cacheDeIconos = new HashMap<>();
    private ImageIcon iconoDefault;
    public MonedaRenderer() {iconoDefault = crearIconoDefault();}
    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            String codigoMoneda = (String) value;
            setText(codigoMoneda + " - " + MapeoBanderasUtil.getNombreMoneda(codigoMoneda));
            ImageIcon bandera = getIconoBandera(codigoMoneda);
            setIcon(bandera);
        }
        setFont(new Font("Arial", Font.BOLD, 16));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setIconTextGap(10);
        if (isSelected) {
            setBackground(new Color(0, 120, 215));
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }
        return this;
    }
    private ImageIcon getIconoBandera(String codigoMoneda) {
        if (cacheDeIconos.containsKey(codigoMoneda)) {
            return cacheDeIconos.get(codigoMoneda);
        }
        String codigoArchivo = MapeoBanderasUtil.getCodigoBandera(codigoMoneda);
        ImageIcon icono = null;
        if (codigoArchivo != null) {
            String nombreArchivo = codigoArchivo + ".png";
            String rutaEnResources = "/flags/" + nombreArchivo;
            try {
                URL urlImagen = getClass().getResource(rutaEnResources);
                if (urlImagen != null) {
                    ImageIcon iconoOriginal = new ImageIcon(urlImagen);
                    Image img = iconoOriginal.getImage().getScaledInstance(32, 24, Image.SCALE_SMOOTH);
                    icono = new ImageIcon(img);
                } else {
                    icono = iconoDefault;
                }
            } catch (Exception e) {
                e.printStackTrace();
                icono = iconoDefault;
            }
        } else {
            icono = iconoDefault;
        }
        cacheDeIconos.put(codigoMoneda, icono);
        return icono;
    }
    private ImageIcon crearIconoDefault() {
        BufferedImage img = new BufferedImage(32, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillOval(4, 0, 24, 24);
        g2d.setColor(new Color(0, 120, 215));
        g2d.fillOval(12, 8, 8, 8);
        g2d.dispose();
        return new ImageIcon(img);
    }
}