package ec.edu.ups.vista;

import ec.edu.ups.controlador.CarritoController;
import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;

import javax.swing.*;
import java.beans.PropertyVetoException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            ProductoDAO dao               = new ProductoDAOMemoria();
            ProductoView alta             = new ProductoView();
            ProductoListaView lista       = new ProductoListaView();
            ProductoModView mod           = new ProductoModView();
            ProductoElimView elim         = new ProductoElimView(dao);
            CarritoAñadirView carritoView = new CarritoAñadirView();

            ProductoController prodCtrl = new ProductoController(alta, dao, lista);
            prodCtrl.setProductoMod(mod);
            prodCtrl.setProductoElimView(elim);

            CarritoController carritoCtrl = new CarritoController(carritoView, prodCtrl);

            JDesktopPane desktop = new JDesktopPane();

            Principal principal = new Principal();
            desktop.add(principal, JDesktopPane.DEFAULT_LAYER);
            try {
                principal.setMaximum(true);
            } catch (PropertyVetoException ignored) {}

            JFrame frame = new JFrame("Sistema de Carrito de Compras En línea");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setContentPane(desktop);
            frame.setJMenuBar(principal.getRootPane().getJMenuBar());
            frame.setVisible(true);

            principal.getMenuItemCrearProducto().addActionListener(e -> {
                desktop.add(alta, JDesktopPane.DEFAULT_LAYER);
                alta.setVisible(true);
            });
            principal.getMenuItemBuscarProducto().addActionListener(e -> {
                desktop.add(lista, JDesktopPane.DEFAULT_LAYER);
                lista.setVisible(true);
            });
            principal.getMenuItemActualizarProducto().addActionListener(e -> {
                desktop.add(mod, JDesktopPane.DEFAULT_LAYER);
                mod.setVisible(true);
            });
            principal.getMenuItemEliminarProducto().addActionListener(e -> {
                desktop.add(elim, JDesktopPane.DEFAULT_LAYER);
                elim.setVisible(true);
            });
            principal.getMenuItemCarrito().addActionListener(e -> {
                desktop.add(carritoView, JDesktopPane.DEFAULT_LAYER);
                carritoView.setVisible(true);
            });
        });
    }
}
