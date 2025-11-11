package org.example;
import org.example.adapters.in.gui.ConversorVista;
import javax.swing.SwingUtilities;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConversorVista vista = new ConversorVista();
                vista.iniciar();
            }
        });
    }
}
