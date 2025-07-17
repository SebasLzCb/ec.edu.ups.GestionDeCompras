package ec.edu.ups;

import ec.edu.ups.controlador.CarritoController;
import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.controlador.RecuperacionController;
import ec.edu.ups.controlador.UsuarioController;
import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.RecuperacionDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.CarritoDAOMemoria;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;
import ec.edu.ups.dao.impl.RecuperacionDAOMemoria;
import ec.edu.ups.dao.impl.UsuarioDAOMemoria;
import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.InicioDeSesion.LoginView;
import ec.edu.ups.vista.InicioDeSesion.RecuperarContraseñaView;
import ec.edu.ups.vista.InicioDeSesion.RegistroView;
import ec.edu.ups.vista.Principal;
import ec.edu.ups.vista.Producto.ProductoAñadirView;
import ec.edu.ups.vista.Producto.ProductoElimView;
import ec.edu.ups.vista.Producto.ProductoListaView;
import ec.edu.ups.vista.Producto.ProductoModView;
import ec.edu.ups.vista.Carrito.CarritoAñadirView;
import ec.edu.ups.vista.Carrito.CarritoDetallesView;
import ec.edu.ups.vista.Carrito.CarritoElimView;
import ec.edu.ups.vista.Carrito.CarritoListaView;
import ec.edu.ups.vista.Carrito.CarritoModView;
import ec.edu.ups.vista.Usuario.UsuarioElimView;
import ec.edu.ups.vista.Usuario.UsuarioListaView;
import ec.edu.ups.vista.Usuario.UsuarioModView;
import ec.edu.ups.vista.Usuario.UsuarioRegistroView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.List;

public class Main {

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            setLookAndFeel();

            MensajeInternacionalizacionHandler mensajeHandler = new MensajeInternacionalizacionHandler("es", "EC");

            UsuarioDAO usuarioDAO = new UsuarioDAOMemoria();
            ProductoDAO productoDAO = new ProductoDAOMemoria();
            CarritoDAO carritoDAO = new CarritoDAOMemoria();
            RecuperacionDAO recuperacionDAO = new RecuperacionDAOMemoria(usuarioDAO);

            RecuperacionController recuperacionController = new RecuperacionController(recuperacionDAO, usuarioDAO, mensajeHandler);
            Principal principal = new Principal(mensajeHandler);
            LoginView loginView = new LoginView(recuperacionController, mensajeHandler);

            RegistroView registroFrame = new RegistroView(mensajeHandler);
            UsuarioRegistroView registroViewInternal = new UsuarioRegistroView(mensajeHandler);
            loginView.setRegistroFrame(registroFrame);
            recuperacionController.setLoginView(loginView);

            registroFrame.getBtnCrear().addActionListener(ev -> {
                String usr = registroFrame.getTxtUsuario();
                if (usr.isEmpty() || registroFrame.getTxtPassword().isEmpty()) {
                    registroFrame.mostrarMensaje("Usuario y contraseña son obligatorios.");
                    return;
                }
                if (usuarioDAO.buscarPorUsername(usr) != null) {
                    registroFrame.mostrarMensaje("El nombre de usuario ya existe.");
                    return;
                }

                Usuario nuevoUsuario = new Usuario(usr, registroFrame.getTxtPassword(), Rol.USUARIO);
                usuarioDAO.crear(nuevoUsuario);

                String[] preguntas = {
                        registroFrame.getCbxPregunta1().getSelectedItem().toString(),
                        registroFrame.getCbxPregunta2().getSelectedItem().toString(),
                        registroFrame.getCbxPregunta3().getSelectedItem().toString()
                };
                String[] respuestas = {
                        registroFrame.getTxtRespuesta1(),
                        registroFrame.getTxtRespuesta2(),
                        registroFrame.getTxtRespuesta3()
                };
                recuperacionDAO.guardarRespuestas(nuevoUsuario, preguntas, respuestas);

                registroFrame.mostrarMensaje("Usuario registrado con éxito.");
                registroFrame.dispose();
            });

            UsuarioListaView listaView = new UsuarioListaView(mensajeHandler);
            UsuarioModView modView = new UsuarioModView(mensajeHandler);
            UsuarioElimView elimView = new UsuarioElimView(mensajeHandler);

            UsuarioController usuarioController = new UsuarioController(
                    usuarioDAO, loginView, registroViewInternal, listaView, modView, elimView,
                    principal, recuperacionController, mensajeHandler
            );
            ProductoAñadirView prodAddView = new ProductoAñadirView(mensajeHandler);
            ProductoListaView prodListView = new ProductoListaView(mensajeHandler);
            ProductoModView prodModView = new ProductoModView(mensajeHandler);
            ProductoElimView prodElimView = new ProductoElimView(productoDAO, mensajeHandler);

            new ProductoController(productoDAO, prodAddView, prodListView, prodModView, prodElimView, mensajeHandler);

            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Usuario actual = usuarioController.getUsuarioActual();
                    if (actual == null) System.exit(0);

                    CarritoAñadirView cartAddView = new CarritoAñadirView(mensajeHandler);
                    CarritoListaView cartListView = new CarritoListaView(mensajeHandler);
                    CarritoModView cartModView = new CarritoModView(mensajeHandler);
                    CarritoElimView cartElimView = new CarritoElimView(mensajeHandler);
                    CarritoDetallesView cartDetView = new CarritoDetallesView(mensajeHandler);

                    new CarritoController(
                            carritoDAO, productoDAO, cartAddView, cartListView, cartModView,
                            cartElimView, cartDetView, actual, mensajeHandler
                    );

                    principal.setVisible(true);
                    String plantilla = mensajeHandler.get("info.bienvenida");
                    String saludo = MessageFormat.format(plantilla, actual.getUsername());
                    principal.mostrarMensaje(saludo);
                    if (actual.getRol() == Rol.USUARIO) {
                        principal.deshabilitarMenusAdministrador();
                    }

                    principal.getMenuItemCrearProducto().addActionListener(ev -> mostrarVentana(principal, prodAddView));
                    principal.getMenuItemBuscarProducto().addActionListener(ev -> mostrarVentana(principal, prodListView));
                    principal.getMenuItemActualizarProducto().addActionListener(ev -> mostrarVentana(principal, prodModView));
                    principal.getMenuItemEliminarProducto().addActionListener(ev -> mostrarVentana(principal, prodElimView));

                    principal.getMenuItemCrearCarrito().addActionListener(ev -> mostrarVentana(principal, cartAddView));
                    principal.getMenuItemBuscarCarrito().addActionListener(ev -> mostrarVentana(principal, cartListView));
                    principal.getMenuItemModificarCarrito().addActionListener(ev -> mostrarVentana(principal, cartModView));
                    principal.getMenuItemEliminarCarrito().addActionListener(ev -> mostrarVentana(principal, cartElimView));
                    principal.getMenuItemVerDetallesCarrito().addActionListener(ev -> mostrarVentana(principal, cartDetView));

                    principal.getMenuItemIdiomaEspanol().addActionListener(ev -> principal.cambiarIdioma("es", "EC"));
                    principal.getMenuItemIdiomaIngles().addActionListener(ev -> principal.cambiarIdioma("en", "US"));
                    principal.getMenuItemIdiomaFrances().addActionListener(ev -> principal.cambiarIdioma("fr", "FR"));
                }
            });

            // 5. Inicia la aplicación mostrando la ventana de login
            loginView.setVisible(true);
        });
    }

    private static void mostrarVentana(Principal principal, JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            if (ventana.getParent() == null) {
                principal.getDesktopPane().add(ventana);
            }
            ventana.setVisible(true);
        }
        try {
            ventana.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {

        }
        ventana.toFront();
    }
}