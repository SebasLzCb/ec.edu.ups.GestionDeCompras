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
import ec.edu.ups.dao.impl.CarritoDAOArchivoTexto;
import ec.edu.ups.dao.impl.ProductoDAOArchivoTexto;
import ec.edu.ups.dao.impl.RecuperacionDAOArchivoTexto;
import ec.edu.ups.dao.impl.UsuarioDAOArchivoTexto;

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

            UsuarioDAO tempUsuarioDAO = new UsuarioDAOMemoria();
            RecuperacionDAO tempRecuperacionDAO = new RecuperacionDAOMemoria(tempUsuarioDAO);

            RecuperacionController recuperacionController = new RecuperacionController(tempRecuperacionDAO, tempUsuarioDAO, mensajeHandler);
            LoginView loginView = new LoginView(recuperacionController, mensajeHandler);

            recuperacionController.setLoginView(loginView);

            RecuperarContraseñaView recuperarContraseniaView = new RecuperarContraseñaView(mensajeHandler, recuperacionController.obtenerPreguntasLocalizadas());
            recuperacionController.setRecuperarView(recuperarContraseniaView);

            Principal principal = new Principal(mensajeHandler);
            loginView.setPrincipal(principal);

            // Inicializamos ambas instancias de vista de registro
            RegistroView registroJFrame = new RegistroView(mensajeHandler); // JFrame
            UsuarioRegistroView registroJInternalFrame = new UsuarioRegistroView(mensajeHandler); // JInternalFrame

            loginView.setRegistroFrame(registroJFrame); // LoginView sigue abriendo este JFrame

            loginView.setVisible(true);

            loginView.addLoginActionListener(e -> {
                String selectedStorageType = loginView.getSelectedStorageType();

                UsuarioDAO finalUsuarioDAO;
                ProductoDAO finalProductoDAO;
                CarritoDAO finalCarritoDAO;
                RecuperacionDAO finalRecuperacionDAO;

                if (mensajeHandler.get("login.storage.memory").equals(selectedStorageType)) {
                    System.out.println("INFO: Usando almacenamiento en memoria.");
                    finalUsuarioDAO = new UsuarioDAOMemoria();
                    finalProductoDAO = new ProductoDAOMemoria();
                    finalCarritoDAO = new CarritoDAOMemoria();
                    finalRecuperacionDAO = new RecuperacionDAOMemoria(finalUsuarioDAO);
                } else if (mensajeHandler.get("login.storage.file_system").equals(selectedStorageType)) {
                    System.out.println("INFO: Usando almacenamiento en archivos de texto.");
                    finalUsuarioDAO = new UsuarioDAOArchivoTexto();
                    finalProductoDAO = new ProductoDAOArchivoTexto();
                    finalCarritoDAO = new CarritoDAOArchivoTexto();
                    finalRecuperacionDAO = new RecuperacionDAOArchivoTexto(finalUsuarioDAO);
                } else {
                    System.err.println("ADVERTENCIA: Tipo de almacenamiento no reconocido o no seleccionado. Usando memoria por defecto.");
                    finalUsuarioDAO = new UsuarioDAOMemoria();
                    finalProductoDAO = new ProductoDAOMemoria();
                    finalCarritoDAO = new CarritoDAOMemoria();
                    finalRecuperacionDAO = new RecuperacionDAOMemoria(finalUsuarioDAO);
                }

                recuperacionController.setUsuarioDAO(finalUsuarioDAO);
                recuperacionController.setRecuperacionDAO(finalRecuperacionDAO);

                String usernameInput = loginView.getTxtUsername().getText().trim();
                String passwordInput = new String(loginView.getTxtContrasenia().getPassword()).trim();
                Usuario actual = finalUsuarioDAO.autenticar(usernameInput, passwordInput);

                if (actual == null) {
                    loginView.mostrarMensaje(mensajeHandler.get("usuario.error.credenciales"));
                    return;
                }

                loginView.dispose();

                UsuarioListaView listaView = new UsuarioListaView(mensajeHandler);
                UsuarioModView modView = new UsuarioModView(mensajeHandler);
                UsuarioElimView elimView = new UsuarioElimView(mensajeHandler);

                // CAMBIO: Pasa AMBAS instancias de vista de registro (registroJFrame y registroJInternalFrame)
                UsuarioController usuarioControllerInstancia = new UsuarioController(
                        finalUsuarioDAO, loginView, registroJFrame, registroJInternalFrame, // PASA registroJFrame y registroJInternalFrame
                        listaView, modView, elimView,
                        principal, recuperacionController, mensajeHandler
                );
                usuarioControllerInstancia.setUsuarioActual(actual);

                ProductoAñadirView prodAddView = new ProductoAñadirView(mensajeHandler);
                ProductoListaView prodListView = new ProductoListaView(mensajeHandler);
                ProductoModView prodModView = new ProductoModView(mensajeHandler);
                ProductoElimView prodElimView = new ProductoElimView(finalProductoDAO, mensajeHandler);

                new ProductoController(finalProductoDAO, prodAddView, prodListView, prodModView, prodElimView, mensajeHandler);

                CarritoAñadirView cartAddView = new CarritoAñadirView(mensajeHandler);
                CarritoListaView cartListView = new CarritoListaView(mensajeHandler);
                CarritoModView cartModView = new CarritoModView(mensajeHandler);
                CarritoElimView cartElimView = new CarritoElimView(mensajeHandler);
                CarritoDetallesView cartDetView = new CarritoDetallesView(mensajeHandler);

                new CarritoController(
                        finalCarritoDAO, finalProductoDAO, cartAddView, cartListView, cartModView,
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
            });

            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (!principal.isVisible()) {
                        System.exit(0);
                    }
                }
            });
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