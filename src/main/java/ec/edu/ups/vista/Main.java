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
import ec.edu.ups.vista.Usuario.*;
import ec.edu.ups.vista.Producto.*;
import ec.edu.ups.vista.Carrito.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            UsuarioDAO usuarioDAO = new UsuarioDAOMemoria();

            // Vistas de login y usuario
            LoginView loginView = new LoginView();
            UsuarioListaView listaUsuarios = new UsuarioListaView();
            UsuarioRegistroView registroView = new UsuarioRegistroView();
            UsuarioModView modView = new UsuarioModView();
            UsuarioElimView elimView = new UsuarioElimView();
            Principal principal = new Principal(); // Se mueve aquí

            // Controlador de usuarios con el principal
            UsuarioController usuarioController = new UsuarioController(
                    loginView, usuarioDAO, listaUsuarios, registroView, modView, elimView, principal
            );

            loginView.setVisible(true);

            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Usuario actual = usuarioController.getUsuarioActual();

                    if (actual == null) {
                        System.exit(0);
                    }

                    ProductoDAO productoDAO = new ProductoDAOMemoria();
                    CarritoDAO carritoDAO = new CarritoDAOMemoria();

                    ProductoAñadirView productoAnadirView = new ProductoAñadirView();
                    ProductoListaView productoListaView = new ProductoListaView();
                    ProductoModView productoModView = new ProductoModView();
                    ProductoElimView productoElimView = new ProductoElimView(productoDAO);

                    CarritoAñadirView carritoAnadirView = new CarritoAñadirView();
                    CarritoListaView carritoListaView = new CarritoListaView();
                    CarritoModView carritoModView = new CarritoModView();

                    principal.setVisible(true);

                    ProductoController productoController = new ProductoController(
                            productoDAO,
                            productoAnadirView,
                            productoListaView,
                            carritoAnadirView
                    );
                    productoController.setProductoMod(productoModView);
                    productoController.setProductoElimView(productoElimView);

                    new CarritoController(carritoDAO, productoDAO, carritoAnadirView, carritoListaView, carritoModView, actual);

                    principal.mostrarMensaje("Bienvenido: " + actual.getUsername());
                    if (actual.getRol() == Rol.USUARIO) {
                        principal.deshabilitarMenusAdministrador();
                    }

                    principal.getMenuItemCrearProducto().addActionListener(ev1 -> {
                        if (!productoAnadirView.isVisible()) {
                            principal.getDesktopPane().add(productoAnadirView);
                        }
                        productoAnadirView.setVisible(true);
                    });

                    principal.getMenuItemBuscarProducto().addActionListener(ev2 -> {
                        if (!productoListaView.isVisible()) {
                            principal.getDesktopPane().add(productoListaView);
                        }
                        productoListaView.setVisible(true);
                    });

                    principal.getMenuItemActualizarProducto().addActionListener(ev3 -> {
                        if (!productoModView.isVisible()) {
                            principal.getDesktopPane().add(productoModView);
                        }
                        productoModView.setVisible(true);
                    });

                    principal.getMenuItemEliminarProducto().addActionListener(ev4 -> {
                        if (!productoElimView.isVisible()) {
                            principal.getDesktopPane().add(productoElimView);
                        }
                        productoElimView.setVisible(true);
                    });

                    principal.getMenuItemCrearCarrito().addActionListener(ev5 -> {
                        if (!carritoAnadirView.isVisible()) {
                            principal.getDesktopPane().add(carritoAnadirView);
                        }
                        carritoAnadirView.setVisible(true);
                    });

                    principal.getMenuItemBuscarCarrito().addActionListener(ev6 -> {
                        if (!carritoListaView.isVisible()) {
                            principal.getDesktopPane().add(carritoListaView);
                        }
                        carritoListaView.setVisible(true);
                    });

                    principal.getMenuItemModificarCarrito().addActionListener(ev7 -> {
                        if (!carritoModView.isVisible()) {
                            principal.getDesktopPane().add(carritoModView);
                        }
                        carritoModView.setVisible(true);
                    });

                    // CORREGIDO: Eliminar usuario abre elimView
                    principal.getMenuItemEliminarUsuario().addActionListener(ev8 -> {
                        if (!elimView.isVisible()) {
                            principal.getDesktopPane().add(elimView);
                        }
                        elimView.setVisible(true);
                    });

                    principal.getMenuItemCerrarSesion().addActionListener(ev9 -> {
                        principal.dispose();
                        main(null); // reiniciar app
                    });
                }
            });
        });
    }
}
