package ec.edu.ups.controlador;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.LoginView;
import ec.edu.ups.vista.Principal;
import ec.edu.ups.vista.Usuario.*;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsuarioController {

    private Usuario usuarioActual;
    private final UsuarioDAO usuarioDAO;
    private final LoginView loginView;
    private final UsuarioRegistroView registroView;
    private final UsuarioListaView listaView;
    private final UsuarioModView modView;
    private final UsuarioElimView elimView;
    private final Principal principal;

    public UsuarioController(UsuarioDAO usuarioDAO,
                             LoginView loginView,
                             UsuarioRegistroView registroView,
                             UsuarioListaView listaView,
                             UsuarioModView modView,
                             UsuarioElimView elimView,
                             Principal principal) {
        this.usuarioDAO = usuarioDAO;
        this.loginView = loginView;
        this.registroView = registroView;
        this.listaView = listaView;
        this.modView = modView;
        this.elimView = elimView;
        this.principal = principal;

        configurarEventos();
    }

    private void configurarEventos() {
        loginView.getBtnIniciarSesion().addActionListener(e -> autenticar());

        loginView.getBtnRegistrar().addActionListener(e -> registroView.setVisible(true));

        registroView.getBtnCrear().addActionListener(e -> registrar());

        listaView.getBtnBuscar().addActionListener(e -> buscarEnLista());
        listaView.getBtnRefrescar().addActionListener(e -> refrescarTablas());

        modView.getBtnActualizar().addActionListener(e -> actualizarUsuario());

        elimView.getBtnEliminar().addActionListener(e -> eliminarUsuario());

        principal.getMenuItemRegistrarUsuario().addActionListener(e -> abrirVentana(registroView));
        principal.getMenuItemListarUsuarios().addActionListener(e -> {
            refrescarTablas();
            abrirVentana(listaView);
        });
        principal.getMenuItemModificarUsuario().addActionListener(e -> {
            refrescarTablas();
            abrirVentana(modView);
        });
        principal.getMenuItemEliminarUsuario().addActionListener(e -> {
            refrescarTablas();
            abrirVentana(elimView);
        });

        principal.getMenuItemCerrarSesion().addActionListener(e -> {
            principal.setVisible(false);
            loginView.limpiarCampos();
            loginView.setVisible(true);
        });
    }

    private void autenticar() {
        String usuario = loginView.getTxtUsername().getText().trim();
        String contrasenia = loginView.getTxtContrasenia().getText().trim();
        usuarioActual = usuarioDAO.autenticar(usuario, contrasenia);

        if (usuarioActual == null) {
            loginView.mostrarMensaje("Usuario o contraseña incorrectos.");
        } else {
            loginView.dispose();
            loginView.dispatchEvent(new java.awt.event.WindowEvent(loginView, java.awt.event.WindowEvent.WINDOW_CLOSED));
        }
    }

    private void registrar() {
        String usuario = registroView.getTxtUsuario().getText().trim();
        String contrasenia = new String(registroView.getTxtPassword().getPassword()).trim();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            registroView.mostrarMensaje("No puede dejar campos vacíos.");
            return;
        }

        Usuario nuevo = new Usuario(usuario, contrasenia, Rol.USUARIO);
        usuarioDAO.crear(nuevo);
        registroView.setVisible(false);
        refrescarTablas();
    }

    private void actualizarUsuario() {
        String usuario = modView.getTxtUsername().getText().trim();
        String contrasenia = new String(modView.getTxtPassword().getPassword()).trim();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            modView.mostrarMensaje("Campos obligatorios");
            return;
        }

        Rol rol = Rol.valueOf(modView.getCbxRol().getSelectedItem().toString());
        usuarioDAO.actualizar(new Usuario(usuario, contrasenia, rol));
        modView.limpiarCampos();
        refrescarTablas();
    }

    private void eliminarUsuario() {
        int fila = elimView.getTableUsuarios().getSelectedRow();
        if (fila >= 0) {
            String nombre = elimView.getTableModel().getValueAt(fila, 0).toString();
            usuarioDAO.eliminar(nombre);
            refrescarTablas();
        } else {
            elimView.mostrarMensaje("Seleccione un usuario.");
        }
    }

    private void buscarEnLista() {
        String texto = listaView.getTxtBuscar().getText().trim().toLowerCase();
        String filtro = listaView.getComboFiltro().getSelectedItem().toString();
        DefaultTableModel model = listaView.getTableModel();
        model.setRowCount(0);

        for (Usuario u : usuarioDAO.listarTodos()) {
            if ((filtro.equalsIgnoreCase("Username") && u.getUsername().toLowerCase().contains(texto)) ||
                    (filtro.equalsIgnoreCase("Rol") && u.getRol().toString().toLowerCase().contains(texto))) {
                model.addRow(new Object[]{u.getUsername(), u.getRol()});
            }
        }
    }

    private void refrescarTablas() {
        DefaultTableModel modelLista = listaView.getTableModel();
        modelLista.setRowCount(0);
        DefaultTableModel modelElim = elimView.getTableModel();
        modelElim.setRowCount(0);

        for (Usuario u : usuarioDAO.listarTodos()) {
            Object[] fila = new Object[]{u.getUsername(), u.getRol()};
            modelLista.addRow(fila);
            modelElim.addRow(fila);
        }
    }

    private void abrirVentana(javax.swing.JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            principal.getDesktopPane().add(ventana);
        }
        ventana.setVisible(true);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
