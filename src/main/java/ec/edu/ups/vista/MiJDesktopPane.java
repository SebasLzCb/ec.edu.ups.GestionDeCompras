package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class MiJDesktopPane extends JDesktopPane {

    // contador de fotogramas para la animación
    private int animFrame = 0;

    public MiJDesktopPane() {
        // Cada 80ms incrementa animFrame y repinta
        new Timer(80, e -> {
            animFrame++;
            repaint();
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int W = getWidth(), H = getHeight();

        // — 1) Fondo y glow —
        GradientPaint fondo = new GradientPaint(0, 0, new Color(10, 10, 20),
                0, H, new Color(5, 5, 10));
        g2.setPaint(fondo);
        g2.fillRect(0, 0, W, H);

        int glowR = W / 3;
        RadialGradientPaint glow = new RadialGradientPaint(
                new Point2D.Float(W / 2f, H / 2f - 30), glowR,
                new float[]{0f, 1f},
                new Color[]{new Color(180, 50, 255, 150), new Color(5, 5, 10, 0)}
        );
        g2.setPaint(glow);
        g2.fillOval(W / 2 - glowR, H / 2 - 30 - glowR, glowR * 2, glowR * 2);

        // — 2) Escritorio —
        int deskH = H / 4;
        g2.setColor(new Color(30, 30, 40));
        g2.fillRect(0, H - deskH, W, deskH);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
        g2.setPaint(new GradientPaint(0, H - deskH, new Color(255, 255, 255, 50),
                0, H, new Color(255, 255, 255, 0)));
        g2.fillRect(0, H - deskH, W, deskH);
        g2.setComposite(AlphaComposite.SrcOver);

        // — 3) Monitor —
        int mW = W / 2, mH = H / 2;
        int mX = W / 2 - mW / 2, mY = H / 2 - mH / 2 - 20;
        int pad = 20;

        g2.setColor(new Color(40, 40, 50));
        g2.fillRoundRect(mX, mY, mW, mH, 20, 20);

        g2.setColor(new Color(15, 15, 30));
        g2.fillRoundRect(mX + pad, mY + pad, mW - 2 * pad, mH - 2 * pad, 10, 10);

        g2.setColor(new Color(40, 40, 50).darker());
        g2.fillRect(mX + mW / 2 - 10, mY + mH, 20, 40);
        g2.fillRect(mX + mW / 2 - 50, mY + mH + 40, 100, 10);

        // — 4) Carrito dentro de la pantalla —
        int sx = mX + pad, sy = mY + pad, sW = mW - 2 * pad, sH = mH - 2 * pad;
        int cx = sx + sW / 4, cy = sy + sH / 2 + 10;
        int cartH = sH / 4, cartW = sW / 2;

        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.WHITE);
        // mango
        g2.drawLine(cx, cy - cartH / 2, cx + cartW / 6, cy - cartH / 2 - cartH / 4);
        // armazón
        g2.drawLine(cx + cartW / 6, cy - cartH / 2 - cartH / 4,
                cx + cartW, cy - cartH / 2 - cartH / 4);
        g2.drawLine(cx + cartW, cy - cartH / 2 - cartH / 4,
                cx + cartW - cartW / 6, cy + cartH / 2 - cartH / 4);
        g2.drawLine(cx + cartW / 6, cy + cartH / 2 - cartH / 4,
                cx + cartW - cartW / 6, cy + cartH / 2 - cartH / 4);
        g2.drawLine(cx + cartW / 6, cy - cartH / 2 - cartH / 4,
                cx + cartW / 6, cy + cartH / 2 - cartH / 4);

        // rejilla
        g2.setStroke(new BasicStroke(2f));
        for (int i = 1; i <= 3; i++) {
            int x = cx + cartW / 6 + i * ((cartW - 2 * (cartW / 6)) / 4);
            g2.drawLine(x, cy - cartH / 2 - cartH / 4 + 5,
                    x, cy + cartH / 2 - cartH / 4 - 5);
        }
        int yGrid = cy - cartH / 2 - cartH / 4 + cartH / 2;
        g2.drawLine(cx + cartW / 6 + 5, yGrid,
                cx + cartW - cartW / 6 - 5, yGrid);

        // ruedas
        int r = cartH / 3;
        g2.fillOval(cx + cartW / 6 - r, cy + cartH / 2 - cartH / 4, r * 2, r * 2);
        g2.fillOval(cx + (5 * cartW / 6) - r, cy + cartH / 2 - cartH / 4, r * 2, r * 2);

        // — 5) Teclado —
        int kbW = mW / 2, kbH = 30;
        int kbX = mX + mW / 2 - kbW / 2, kbY = mY + mH + 50;

        g2.setColor(new Color(80, 80, 90));
        g2.fillRoundRect(kbX, kbY, kbW, kbH, 10, 10);

        g2.setColor(new Color(110, 110, 120));
        int cols = 14, rows = 2;
        int keyW = (kbW - 16) / cols, keyH = (kbH - 12) / rows;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = kbX + 8 + col * keyW, y = kbY + 6 + row * keyH;
                g2.fillRoundRect(x, y, keyW - 6, keyH - 6, 4, 4);
            }
        }
        // barra espaciadora
        int sp = keyW * 5 + 4;
        g2.setColor(new Color(130, 130, 140));
        g2.fillRoundRect(kbX + (kbW - sp) / 2, kbY + kbH - keyH - 4,
                sp, keyH - 6, 4, 4);

        // — 6) Mouse —
        int mXo = kbX + kbW + 30, mYo = kbY - 5;
        g2.setColor(new Color(80, 80, 90));
        g2.fillOval(mXo, mYo, 22, 32);
        g2.fillOval(mXo + 4, mYo - 6, 14, 44);

        g2.dispose();
    }
}
