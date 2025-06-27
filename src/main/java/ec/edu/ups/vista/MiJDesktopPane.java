package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;

public class MiJDesktopPane extends JDesktopPane {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(8));

        // ====== PERSONA ======

        // Cabeza
        g2.fillOval(cx - 150, cy - 150, 50, 50);

        // Cuerpo (tórax)
        g2.fillRoundRect(cx - 140, cy - 110, 30, 80, 20, 20);

        // Brazo que empuja (diagonal al carrito)
        g2.fillRoundRect(cx - 115, cy - 100, 80, 20, 20, 20);

        // Pierna derecha hacia adelante
        g2.fillRoundRect(cx - 120, cy - 30, 25, 70, 20, 20);
        g2.fillRoundRect(cx - 95, cy + 40, 50, 20, 20, 20); // pie

        // Pierna izquierda hacia atrás
        g2.fillRoundRect(cx - 160, cy - 30, 25, 50, 20, 20);
        g2.fillRoundRect(cx - 180, cy + 10, 50, 20, 20, 20); // pie

        // ====== CARRITO DE COMPRAS ======

        int cartX = cx - 10;
        int cartY = cy - 90;
        int cartW = 200;
        int cartH = 110;

        g2.drawRoundRect(cartX, cartY, cartW, cartH, 20, 20); // marco

        // Mango
        g2.drawLine(cx - 35, cy - 95, cartX, cartY + 10);

        // Ruedas
        g2.fillOval(cartX + 15, cartY + cartH + 10, 25, 25);
        g2.fillOval(cartX + cartW - 40, cartY + cartH + 10, 25, 25);

        // Rejilla interna (verticales)
        for (int i = 1; i < 5; i++) {
            int x = cartX + i * cartW / 5;
            g2.drawLine(x, cartY, x, cartY + cartH);
        }

        // Rejilla interna (horizontales)
        for (int i = 1; i < 3; i++) {
            int y = cartY + i * cartH / 3;
            g2.drawLine(cartX, y, cartX + cartW, y);
        }
    }
}
