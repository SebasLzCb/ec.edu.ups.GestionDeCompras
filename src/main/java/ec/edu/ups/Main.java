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
import ec.edu.ups.vista.MiJDesktopPane;
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
import javax.swing.JInternalFrame;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.List;

public class Main {

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            setLookAndFeel();

            MensajeInternacionalizacionHandler mensajeHandler =
                    new MensajeInternacionalizacionHandler("es", "EC");

            UsuarioDAO usuarioDAO           = new UsuarioDAOMemoria();
            ProductoDAO productoDAO         = new ProductoDAOMemoria();
            CarritoDAO carritoDAO           = new CarritoDAOMemoria();
            RecuperacionDAO recuperacionDAO = new RecuperacionDAOMemoria();

            RecuperacionController recuperacionController =
                    new RecuperacionController(recuperacionDAO, usuarioDAO, mensajeHandler);

            MiJDesktopPane desktopPane = new MiJDesktopPane();
            Principal principal = new Principal(mensajeHandler);

            LoginView loginView = new LoginView(
                    recuperacionController,
                    mensajeHandler
            );
            recuperacionController.setLoginView(loginView);

            RegistroView registroFrame = new RegistroView(mensajeHandler);
            loginView.setRegistroFrame(registroFrame);

            registroFrame.getBtnCrear().addActionListener(ev -> {
                String usr = registroFrame.getTxtUsuario();
                String pwd = registroFrame.getTxtPassword();
                if (usr.isEmpty() || pwd.isEmpty()) {
                    registroFrame.mostrarMensaje(
                            mensajeHandler.get("error.usuario.required")
                    );
                    return;
                }
                Usuario nuevo = new Usuario(usr, pwd, Rol.USUARIO);
                usuarioDAO.crear(nuevo);

                List<Respuesta> respuestas = List.of(
                        new Respuesta(
                                usr,
                                new Pregunta(1,
                                        registroFrame.getCbxPregunta1()
                                                .getSelectedItem().toString()),
                                registroFrame.getTxtRespuesta1()
                        ),
                        new Respuesta(
                                usr,
                                new Pregunta(2,
                                        registroFrame.getCbxPregunta2()
                                                .getSelectedItem().toString()),
                                registroFrame.getTxtRespuesta2()
                        ),
                        new Respuesta(
                                usr,
                                new Pregunta(3,
                                        registroFrame.getCbxPregunta3()
                                                .getSelectedItem().toString()),
                                registroFrame.getTxtRespuesta3()
                        )
                );
                recuperacionController.registrarPreguntas(usr, respuestas);

                registroFrame.mostrarMensaje(
                        mensajeHandler.get("usuario.success.registro_exitoso")
                );

                registroFrame.limpiarCampos();
                registroFrame.setVisible(false);
            });

            loginView.setVisible(true);

            UsuarioRegistroView registroView    = new UsuarioRegistroView(mensajeHandler);
            UsuarioListaView    listaView       = new UsuarioListaView(mensajeHandler);
            UsuarioModView      modView         = new UsuarioModView(mensajeHandler);
            UsuarioElimView     elimView        = new UsuarioElimView(mensajeHandler);

            ProductoAñadirView  prodAddView     = new ProductoAñadirView(mensajeHandler);
            ProductoListaView   prodListView    = new ProductoListaView(mensajeHandler);
            ProductoModView     prodModView     = new ProductoModView(mensajeHandler);
            ProductoElimView    prodElimView    = new ProductoElimView(productoDAO, mensajeHandler);

            CarritoAñadirView   cartAddView     = new CarritoAñadirView(mensajeHandler);
            CarritoListaView    cartListView    = new CarritoListaView(mensajeHandler);
            CarritoModView      cartModView     = new CarritoModView(mensajeHandler);
            CarritoElimView     cartElimView    = new CarritoElimView(mensajeHandler);
            CarritoDetallesView cartDetView     = new CarritoDetallesView(mensajeHandler);

            UsuarioController usuarioController = new UsuarioController(
                    usuarioDAO,
                    loginView,
                    registroView,
                    listaView,
                    modView,
                    elimView,
                    principal,
                    recuperacionController,
                    mensajeHandler
            );

            new ProductoController(
                    productoDAO,
                    prodAddView,
                    prodListView,
                    prodModView,
                    prodElimView,
                    mensajeHandler
            );

            loginView.getBtnOlvCont().addActionListener(ev -> {
                String user = loginView.getTxtUsername().getText().trim();
                if (user.isEmpty()) {
                    loginView.mostrarMensaje
                            (mensajeHandler.get("error.usuario.required"));
                    return;
                }
                List<String> hechas = recuperacionDAO.getPreguntasUsuario(user);
                if (hechas.isEmpty()) {
                    loginView.mostrarMensaje
                            (mensajeHandler.get("error.recuperacion.sin_respuestas"));
                    return;
                }
                RecuperarContraseñaView recView =
                        new RecuperarContraseñaView(user, hechas, mensajeHandler);
                recuperacionController.setRecuperarView(recView);
                recuperacionController.mostrarRecuperar(user);
            });

            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Usuario actual = usuarioController.getUsuarioActual();
                    if (actual == null) System.exit(0);

                    new CarritoController(
                            carritoDAO,
                            productoDAO,
                            cartAddView,
                            cartListView,
                            cartModView,
                            cartElimView,
                            cartDetView,
                            actual, mensajeHandler
                    );

                    principal.setVisible(true);
                    String plantilla = mensajeHandler.get("info.bienvenida");
                    String saludo    = MessageFormat.format(plantilla, actual.getUsername());
                    principal.mostrarMensaje(saludo);
                    if (actual.getRol() == Rol.USUARIO) {
                        principal.deshabilitarMenusAdministrador();
                    }

                    principal.getMenuItemCrearProducto()
                            .addActionListener(ev -> mostrarVentana(principal, prodAddView));
                    principal.getMenuItemBuscarProducto()
                            .addActionListener(ev -> mostrarVentana(principal, prodListView));
                    principal.getMenuItemActualizarProducto()
                            .addActionListener(ev -> mostrarVentana(principal, prodModView));
                    principal.getMenuItemEliminarProducto()
                            .addActionListener(ev -> mostrarVentana(principal, prodElimView));

                    principal.getMenuItemCrearCarrito()
                            .addActionListener(ev -> mostrarVentana(principal, cartAddView));
                    principal.getMenuItemBuscarCarrito()
                            .addActionListener(ev -> mostrarVentana(principal, cartListView));
                    principal.getMenuItemModificarCarrito()
                            .addActionListener(ev -> mostrarVentana(principal, cartModView));
                    principal.getMenuItemEliminarCarrito()
                            .addActionListener(ev -> mostrarVentana(principal, cartElimView));
                    principal.getMenuItemVerDetallesCarrito()
                            .addActionListener(ev -> mostrarVentana(principal, cartDetView));

                    principal.getMenuItemListarUsuarios()
                            .addActionListener(ev -> mostrarVentana(principal, listaView));
                    principal.getMenuItemModificarUsuario()
                            .addActionListener(ev -> mostrarVentana(principal, modView));
                    principal.getMenuItemEliminarUsuario()
                            .addActionListener(ev -> mostrarVentana(principal, elimView));
                    principal.getMenuItemRegistrarUsuario()
                            .addActionListener(ev -> {
                                registroView.pack();
                                registroView.setVisible(true);
                            });

                    principal.getMenuItemCerrarSesion()
                            .addActionListener(ev -> {
                                principal.dispose();
                                main(null);
                            });

                    principal.getMenuItemIdiomaEspanol()
                            .addActionListener(ev -> principal.cambiarIdioma("es", "EC"));
                    principal.getMenuItemIdiomaIngles()
                            .addActionListener(ev -> principal.cambiarIdioma("en", "US"));
                    principal.getMenuItemIdiomaFrances()
                            .addActionListener(ev -> principal.cambiarIdioma("fr", "FR"));
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
        ventana.toFront();
    }
}
