package ec.edu.ups.controlador;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.LoginView;
import ec.edu.ups.vista.Principal;
import ec.edu.ups.vista.Usuario.UsuarioElimView;
import ec.edu.ups.vista.Usuario.UsuarioListaView;
import ec.edu.ups.vista.Usuario.UsuarioModView;
import ec.edu.ups.vista.Usuario.UsuarioRegistroView;

import javax.swing.table.DefaultTableModel;

public class UsuarioController {

    private Usuario usuarioActual;
    private final UsuarioDAO usuarioDAO;
    private final LoginView loginView;
    private final UsuarioListaView listaView;
    private final UsuarioRegistroView registroView;
    private final UsuarioModView modView;
    private final UsuarioElimView elimView;
    private final Principal principal;

    public UsuarioController(LoginView loginView,
                             UsuarioDAO usuarioDAO,
                             UsuarioListaView listaView,
                             UsuarioRegistroView registroView,
                             UsuarioModView modView,
                             UsuarioElimView elimView,
                             Principal principal) {

        this.loginView = loginView;
        this.usuarioDAO = usuarioDAO;
        this.listaView = listaView;
        this.registroView = registroView;
        this.modView = modView;
        this.elimView = elimView;
        this.principal = principal;

        configurarEventos();
    }

    private void configurarEventos() {
        // LOGIN
        loginView.getBtnIniciarSesion().addActionListener(e -> {
            String username = loginView.getTxtUsername().getText().trim();
            String password = loginView.getTxtContrasenia().getText().trim();
            usuarioActual = usuarioDAO.autenticar(username, password);

            if (usuarioActual == null) {
                loginView.mostrarMensaje("Usuario o contraseña incorrectos.");
            } else {
                loginView.dispose();
                principal.setVisible(true);

                if (usuarioActual.getRol() == Rol.USUARIO) {
                    principal.deshabilitarMenusAdministrador();
                }
            }
        });

        loginView.getBtnRegistrar().addActionListener(e -> registroView.setVisible(true));

        // REGISTRO
        registroView.getBtnCrear().addActionListener(e -> {
            String usuario = registroView.getTxtUsuario().getText().trim();
            String contra = new String(registroView.getTxtPassword().getPassword()).trim();
            Rol rol = Rol.USUARIO;

            if (usuario.isEmpty() || contra.isEmpty()) {
                registroView.mostrarMensaje("No puede dejar campos vacíos.");
                return;
            }

            usuarioDAO.crear(new Usuario(usuario, contra, rol));
            registroView.setVisible(false);
            refrescarTabla();
        });

        // MODIFICACIÓN
        modView.getBtnActualizar().addActionListener(e -> {
            String nombre = modView.getTxtUsername().getText().trim();
            String nuevaContra = new String(modView.getTxtPassword().getPassword()).trim();

            if (modView.getCbxRol().getSelectedItem() == null) {
                modView.mostrarMensaje("Seleccione un rol.");
                return;
            }

            Rol nuevoRol = Rol.valueOf(modView.getCbxRol().getSelectedItem().toString());

            usuarioDAO.actualizar(new Usuario(nombre, nuevaContra, nuevoRol));
            refrescarTabla();
            modView.limpiarCampos();
        });

        // ELIMINACIÓN
        elimView.getBtnEliminar().addActionListener(e -> {
            int fila = elimView.getTableUsuarios().getSelectedRow();
            if (fila >= 0) {
                String nombre = elimView.getTableModel().getValueAt(fila, 0).toString();
                usuarioDAO.eliminar(nombre);
                refrescarTabla();
            } else {
                elimView.mostrarMensaje("Seleccione un usuario.");
            }
        });

        // BÚSQUEDA EN LISTA
        listaView.getBtnBuscar().addActionListener(e -> {
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
        });

        listaView.getBtnRefrescar().addActionListener(e -> refrescarTabla());

        // MENÚ PRINCIPAL → ACCIONES
        principal.getMenuItemRegistrarUsuario().addActionListener(e -> {
            if (!registroView.isVisible()) {
                principal.getDesktopPane().add(registroView);
            }
            registroView.setVisible(true);
        });

        principal.getMenuItemListarUsuarios().addActionListener(e -> {
            refrescarTabla();
            if (!listaView.isVisible()) {
                principal.getDesktopPane().add(listaView);
            }
            listaView.setVisible(true);
        });

        principal.getMenuItemModificarUsuario().addActionListener(e -> {
            refrescarTabla();
            if (!modView.isVisible()) {
                principal.getDesktopPane().add(modView);
            }
            modView.setVisible(true);
        });

        principal.getMenuItemEliminarUsuario().addActionListener(e -> {
            refrescarTabla();
            if (!elimView.isVisible()) {
                principal.getDesktopPane().add(elimView);
            }
            elimView.setVisible(true);
        });

        principal.getMenuItemCerrarSesion().addActionListener(e -> {
            principal.setVisible(false);
            loginView.limpiarCampos();
            loginView.setVisible(true);
        });
    }

    private void refrescarTabla() {
        DefaultTableModel model = listaView.getTableModel();
        model.setRowCount(0);
        for (Usuario u : usuarioDAO.listarTodos()) {
            model.addRow(new Object[]{u.getUsername(), u.getRol()});
        }

        elimView.getTableModel().setRowCount(0);
        for (Usuario u : usuarioDAO.listarTodos()) {
            elimView.getTableModel().addRow(new Object[]{u.getUsername(), u.getRol()});
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
