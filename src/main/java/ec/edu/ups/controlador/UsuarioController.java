package ec.edu.ups.controlador;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.LoginView;
import ec.edu.ups.vista.Usuario.UsuarioElimView;
import ec.edu.ups.vista.Usuario.UsuarioListaView;
import ec.edu.ups.vista.Usuario.UsuarioModView;
import ec.edu.ups.vista.Usuario.UsuarioRegistroView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UsuarioController {

    private final UsuarioDAO usuarioDAO;
    private final LoginView loginView;
    private final UsuarioListaView listaView;
    private final UsuarioRegistroView registroView;
    private final UsuarioModView modView;
    private final UsuarioElimView elimView;

    private Usuario usuarioActual;

    public UsuarioController(LoginView loginView,
                             UsuarioDAO usuarioDAO,
                             UsuarioListaView listaView,
                             UsuarioRegistroView registroView,
                             UsuarioModView modView,
                             UsuarioElimView elimView) {
        this.loginView    = loginView;
        this.usuarioDAO   = usuarioDAO;
        this.listaView    = listaView;
        this.registroView = registroView;
        this.modView      = modView;
        this.elimView     = elimView;

        configurarLogin();
        configurarCrudUsuario();
    }

    private void configurarLogin() {
        loginView.getBtnLogin().addActionListener(e -> {
            String u = loginView.getUsuario();
            String p = loginView.getPassword();
            try {
                // suponemos que autenticar devuelve el Usuario:
                usuarioActual = usuarioDAO.autenticar(u, p);
                loginView.dispose();
            } catch (RuntimeException ex) {
                loginView.mostrarMensaje("Credenciales inválidas");
            }
        });
        loginView.getBtnRegistrar().addActionListener(e -> {
            registroView.setVisible(true);
        });
    }

    private void configurarCrudUsuario() {
        // CREAR
        registroView.getBtnCrear().addActionListener(e -> {
            String u = registroView.getTxtUsuario().getText().trim();
            String p = new String(registroView.getTxtPassword().getPassword());
            usuarioDAO.crear(new Usuario(u, p, Rol.Cliente));
            registroView.dispose();
            refrescarTabla();
        });

        // LISTAR
        listaView.getBtnRefrescar().addActionListener(e -> refrescarTabla());
        listaView.getBtnBuscar().addActionListener(e -> {
            String criterio = listaView.getComboFiltro().getSelectedItem().toString();
            String texto    = listaView.getTxtBuscar().getText().trim();
            if ("Usuario".equals(criterio)) {
                Usuario uObj = usuarioDAO.buscarPorUsername(texto);
                fillTable(uObj == null ? List.of() : List.of(uObj));
            } else {
                refrescarTabla();
            }
        });

        // MODIFICAR
        modView.getBtnActualizar().addActionListener(e -> {
            int row = modView.getTableUsuarios().getSelectedRow();
            if (row < 0) return;
            String u        = (String) modView.getTableModel().getValueAt(row, 0);
            Usuario orig    = usuarioDAO.buscarPorUsername(u);
            String nuevaPwd = new String(modView.getTxtPassword().getPassword());
            Rol    nuevoRol = (Rol) modView.getCbxRol().getSelectedItem();
            orig.setContraseña(nuevaPwd);
            orig.setRol(nuevoRol);
            usuarioDAO.actualizar(orig);
            refrescarTabla();
        });

        // ELIMINAR
        elimView.getBtnEliminar().addActionListener(e -> {
            int row = elimView.getTableUsuarios().getSelectedRow();
            if (row < 0) return;
            String u = (String) elimView.getTableModel().getValueAt(row, 0);
            usuarioDAO.eliminar(u);
            refrescarTabla();
        });
    }

    private void fillTable(List<Usuario> usuarios) {
        DefaultTableModel m = listaView.getTableModel();
        m.setRowCount(0);
        for (Usuario u : usuarios) {
            m.addRow(new Object[]{ u.getNombreDeUsuario(), u.getRol() });
        }
    }

    public void refrescarTabla() {
        fillTable(usuarioDAO.listarTodos());
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
