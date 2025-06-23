// src/main/java/ec/edu/ups/vista/Main.java
package ec.edu.ups.vista;

import ec.edu.ups.controlador.CarritoController;
import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.controlador.UsuarioController;
import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.CarritoDAOMemoria;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;
import ec.edu.ups.dao.impl.UsuarioDAOMemoria;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.Carrito.CarritoAñadirView;
import ec.edu.ups.vista.Producto.*;
import ec.edu.ups.vista.Usuario.*;

import javax.swing.*;
import java.beans.PropertyVetoException;

public class Main {
    public static void main(String[] args) {
        // arrancamos el login directamente:
        UsuarioDAO usuarioDAO     = new UsuarioDAOMemoria();
        LoginView loginView       = new LoginView();
        UsuarioListaView listaV   = new UsuarioListaView();
        UsuarioRegistroView regV  = new UsuarioRegistroView();
        UsuarioModView modV       = new UsuarioModView();
        UsuarioElimView elimV     = new UsuarioElimView();

        // 1) control de login + CRUD usuario
        UsuarioController usrCtrl = new UsuarioController(
                loginView,
                usuarioDAO,
                listaV, regV, modV, elimV
        );

        // 2) mostramos el login y esperamos a que se cierre
        loginView.setVisible(true);
        while (loginView.isVisible()) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }

        // 3) tras login cerrado, recuperamos el usuario autenticado
        Usuario actual = usrCtrl.getUsuarioActual();
        if (actual == null) {
            // si canceló o credenciales fallaron, salimos
            System.exit(0);
        }

        // 4) DAO y vistas de Producto y Carrito
        ProductoDAO prodDAO     = new ProductoDAOMemoria();
        CarritoDAO  cartDAO     = new CarritoDAOMemoria();

        ProductoView        altaP    = new ProductoView();
        ProductoListaView   listaP   = new ProductoListaView();
        ProductoModView     modP     = new ProductoModView();
        ProductoElimView    elimP    = new ProductoElimView(prodDAO);
        CarritoAñadirView   cartAdd  = new CarritoAñadirView();

        // 5) controladores de Producto y Carrito
        ProductoController prodCtrl = new ProductoController(altaP, prodDAO, listaP);
        prodCtrl.setProductoMod(modP);
        prodCtrl.setProductoElimView(elimP);
        prodCtrl.setCarritoView(cartAdd);

        new CarritoController(cartAdd, prodDAO, cartDAO, actual);

        // 6) Ventana principal con JInternalFrames
        Principal principal = new Principal();
        try { principal.setMaximum(true); } catch (PropertyVetoException ignore){}

        JDesktopPane desktop = principal.getDesktop();
        JFrame frame = new JFrame("Sistema de Carrito de Compras");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setContentPane(desktop);
        frame.setJMenuBar(principal.getJMenuBar());
        frame.setVisible(true);

        // 7) Enlazamos menú PRODUCTO
        principal.getMiCrearProducto().addActionListener(e -> {
            if (!altaP.isVisible()) desktop.add(altaP);
            altaP.setVisible(true);
        });
        principal.getMiBuscarProducto().addActionListener(e -> {
            if (!listaP.isVisible()) desktop.add(listaP);
            listaP.setVisible(true);
        });
        principal.getMiActualizarProducto().addActionListener(e -> {
            if (!modP.isVisible()) desktop.add(modP);
            modP.setVisible(true);
        });
        principal.getMiEliminarProducto().addActionListener(e -> {
            if (!elimP.isVisible()) desktop.add(elimP);
            elimP.setVisible(true);
        });

        // 8) Enlazamos menú CARRITO
        principal.getMiCarrito().addActionListener(e -> {
            if (!cartAdd.isVisible()) desktop.add(cartAdd);
            cartAdd.setVisible(true);
        });

        // 9) MENÚ USUARIOS: solo admins
        principal.getMiUsuarios().setEnabled(actual.getRol() == Rol.Administrador);
        principal.getMiUsuarios().addActionListener(e -> {
            if (!listaV.isVisible()) desktop.add(listaV);
            listaV.setVisible(true);
        });

        // 10) CERRAR SESIÓN: cierra el frame y relanza Main
        principal.getMiLogout().addActionListener(e -> {
            frame.dispose();
            main(null);
        });
    }
}
