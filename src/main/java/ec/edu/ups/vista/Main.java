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
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.Producto.*;
import ec.edu.ups.vista.Carrito.*;
import ec.edu.ups.vista.Usuario.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {

            MensajeInternacionalizacionHandler mensajeHandler = new MensajeInternacionalizacionHandler("es", "EC");
            UsuarioDAO usuarioDAO = new UsuarioDAOMemoria();
            ProductoDAO productoDAO = new ProductoDAOMemoria();
            CarritoDAO carritoDAO = new CarritoDAOMemoria();

            LoginView loginView = new LoginView();
            UsuarioRegistroView registroView = new UsuarioRegistroView(mensajeHandler);
            UsuarioListaView listaView = new UsuarioListaView(mensajeHandler);
            UsuarioModView modView = new UsuarioModView(mensajeHandler);
            UsuarioElimView elimView = new UsuarioElimView(mensajeHandler);

            ProductoAñadirView productoAnadirView = new ProductoAñadirView(mensajeHandler);
            ProductoListaView productoListaView = new ProductoListaView(mensajeHandler);
            ProductoModView productoModView = new ProductoModView(mensajeHandler);
            ProductoElimView productoElimView = new ProductoElimView(productoDAO, mensajeHandler);

            CarritoAñadirView carritoAnadirView = new CarritoAñadirView(mensajeHandler);
            CarritoListaView carritoListaView = new CarritoListaView(mensajeHandler);
            CarritoModView carritoModView = new CarritoModView(mensajeHandler);
            CarritoElimView carritoElimView = new CarritoElimView(mensajeHandler);

            Principal principal = new Principal(mensajeHandler);

            UsuarioController usuarioController = new UsuarioController(
                    usuarioDAO, loginView, registroView, listaView, modView, elimView, principal
            );

            new ProductoController(productoDAO, productoAnadirView, productoListaView, productoModView, productoElimView);
            new CarritoController(carritoDAO, productoDAO, carritoAnadirView, carritoListaView, carritoModView, carritoElimView, null);

            loginView.setVisible(true);

            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Usuario actual = usuarioController.getUsuarioActual();
                    if (actual == null) System.exit(0);

                    principal.setVisible(true);
                    principal.mostrarMensaje("Bienvenido: " + actual.getUsername());

                    if (actual.getRol() == Rol.USUARIO) {
                        principal.deshabilitarMenusAdministrador();
                    }

                    principal.getMenuItemCrearProducto().addActionListener(ev -> mostrarVentana(principal, productoAnadirView));
                    principal.getMenuItemBuscarProducto().addActionListener(ev -> mostrarVentana(principal, productoListaView));
                    principal.getMenuItemActualizarProducto().addActionListener(ev -> mostrarVentana(principal, productoModView));
                    principal.getMenuItemEliminarProducto().addActionListener(ev -> mostrarVentana(principal, productoElimView));

                    principal.getMenuItemCrearCarrito().addActionListener(ev -> mostrarVentana(principal, carritoAnadirView));
                    principal.getMenuItemBuscarCarrito().addActionListener(ev -> mostrarVentana(principal, carritoListaView));
                    principal.getMenuItemModificarCarrito().addActionListener(ev -> mostrarVentana(principal, carritoModView));
                    principal.getMenuItemEliminarCarrito().addActionListener(ev -> mostrarVentana(principal, carritoElimView));

                    principal.getMenuItemRegistrarUsuario().addActionListener(ev -> mostrarVentana(principal, registroView));
                    principal.getMenuItemListarUsuarios().addActionListener(ev -> mostrarVentana(principal, listaView));
                    principal.getMenuItemModificarUsuario().addActionListener(ev -> mostrarVentana(principal, modView));
                    principal.getMenuItemEliminarUsuario().addActionListener(ev -> mostrarVentana(principal, elimView));

                    principal.getMenuItemCerrarSesion().addActionListener(ev -> {
                        principal.dispose();
                        main(null);
                    });

                    principal.getMenuItemIdiomaEspanol().addActionListener(ev -> principal.cambiarIdioma("es", "EC"));
                    principal.getMenuItemIdiomaIngles().addActionListener(ev -> principal.cambiarIdioma("en", "US"));
                    principal.getMenuItemIdiomaFrances().addActionListener(ev -> principal.cambiarIdioma("fr", "FR"));
                }
            });
        });
    }

    private static void mostrarVentana(Principal principal, javax.swing.JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            if (ventana.getParent() == null) {
                principal.getDesktopPane().add(ventana);
            }
            ventana.setVisible(true);
        }
        ventana.toFront();
    }
}
