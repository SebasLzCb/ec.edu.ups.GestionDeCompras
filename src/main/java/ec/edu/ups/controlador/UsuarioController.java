package ec.edu.ups.controlador;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.InicioDeSesion.LoginView;
import ec.edu.ups.vista.Principal;
import ec.edu.ups.vista.Usuario.UsuarioRegistroView;
import ec.edu.ups.vista.Usuario.UsuarioListaView;
import ec.edu.ups.vista.Usuario.UsuarioModView;
import ec.edu.ups.vista.Usuario.UsuarioElimView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UsuarioController {

    private Usuario usuarioActual;
    private final UsuarioDAO dao;
    private final LoginView loginView;
    private final UsuarioRegistroView regView;
    private final UsuarioListaView listView;
    private final UsuarioModView modView;
    private final UsuarioElimView elimView;
    private final Principal principal;
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private final ec.edu.ups.controlador.RecuperacionController recuperacionController;

    public UsuarioController(UsuarioDAO dao,
                             LoginView loginView,
                             UsuarioRegistroView regView,
                             UsuarioListaView listView,
                             UsuarioModView modView,
                             UsuarioElimView elimView,
                             Principal principal,
                             ec.edu.ups.controlador.RecuperacionController recuperacionController,
                             MensajeInternacionalizacionHandler mensajeHandler) {
        this.dao                     = dao;
        this.loginView               = loginView;
        this.regView                 = regView;
        this.listView                = listView;
        this.modView                 = modView;
        this.elimView                = elimView;
        this.principal               = principal;
        this.recuperacionController  = recuperacionController;
        this.mensajeHandler          = mensajeHandler;

        configurarVistas();
        configurarEventos();
    }

    private void configurarVistas() {
        listView.setLocation(40, 40);
        modView.setLocation(60, 60);
        elimView.setLocation(80, 80);
    }

    private void configurarEventos() {
        loginView.getBtnIniciarSesion().addActionListener(e -> autenticar());
        loginView.getBtnRegistrar().addActionListener(e -> {
            regView.pack();
            regView.setVisible(true);
        });

        regView.getBtnCrear().addActionListener(e -> registrar());
        regView.getBtnCancelar().addActionListener(e -> regView.setVisible(false));

        listView.getBtnRefrescar().addActionListener(e -> refrescar());
        listView.getBtnBuscar().addActionListener(e -> buscarEnLista());

        principal.getMenuItemModificarUsuario().addActionListener(e -> {
            refrescar();
            abrirInternal(modView);
        });
        modView.getBtnBuscar().addActionListener(e -> refrescar());
        modView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        modView.getBtnCancelar().addActionListener(e -> modView.setVisible(false));

        principal.getMenuItemEliminarUsuario().addActionListener(e -> {
            refrescar();
            abrirInternal(elimView);
        });
        elimView.getBtnBuscar().addActionListener(e -> buscarEnEliminar());
        elimView.getBtnEliminar().addActionListener(e -> eliminarUsuario());

        principal.getMenuItemRegistrarUsuario().addActionListener(ev -> {
            regView.pack();
            regView.setVisible(true);
        });
        principal.getMenuItemListarUsuarios().addActionListener(e -> {
            refrescar();
            abrirInternal(listView);
        });
        principal.getMenuItemCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void autenticar() {
        String usr = loginView.getTxtUsername().getText().trim();
        String pwd = new String(loginView.getTxtContrasenia().getPassword()).trim();
        usuarioActual = dao.autenticar(usr, pwd);
        if (usuarioActual == null) {
            loginView.mostrarMensaje(
                    mensajeHandler.get("usuario.error.credenciales")
            );
        } else {
            loginView.dispose();
            principal.setVisible(true);
        }
    }

    private void registrar() {
        String usr = regView.getTxtUsuario();
        String pwd = regView.getTxtPassword();
        if (usr.isEmpty() || pwd.isEmpty()) {
            regView.mostrarMensaje(
                    mensajeHandler.get("usuario.error.campos_obligatorios")
            );
            return;
        }

        Pregunta p1 = new Pregunta(1, regView.getCbxPregunta1().getSelectedItem().toString());
        Pregunta p2 = new Pregunta(2, regView.getCbxPregunta2().getSelectedItem().toString());
        Pregunta p3 = new Pregunta(3, regView.getCbxPregunta3().getSelectedItem().toString());

        List<Respuesta> respuestas = List.of(
                new Respuesta(usr, p1, regView.getTxtRespuesta1()),
                new Respuesta(usr, p2, regView.getTxtRespuesta2()),
                new Respuesta(usr, p3, regView.getTxtRespuesta3())
        );

        dao.crear(new Usuario(usr, pwd, Rol.USUARIO));
        recuperacionController.registrarPreguntas(usr, respuestas);

        regView.mostrarMensaje(
                mensajeHandler.get("usuario.success.registro_exitoso")
        );
        regView.limpiarCampos();
        regView.setVisible(false);
        refrescar();
    }

    private void refrescar() {
        DefaultTableModel mList = (DefaultTableModel) listView.getTblUsuario().getModel();
        DefaultTableModel mElim = (DefaultTableModel) elimView.getTblUsuarios().getModel();
        mList.setRowCount(0);
        mElim.setRowCount(0);
        for (Usuario u : dao.listarTodos()) {
            Object[] fila = {u.getUsername(), u.getRol()};
            mList.addRow(fila);
            mElim.addRow(fila);
        }
    }

    private void buscarEnLista() {
        String txt = listView.getTxtBuscar().toLowerCase();
        int idx     = listView.getFiltroIndex();
        DefaultTableModel m = (DefaultTableModel) listView.getTblUsuario().getModel();
        m.setRowCount(0);
        for (Usuario u : dao.listarTodos()) {
            boolean ok = (idx == 1)
                    ? u.getRol().toString().toLowerCase().contains(txt)
                    : u.getUsername().toLowerCase().contains(txt);
            if (ok) m.addRow(new Object[]{u.getUsername(), u.getRol()});
        }
    }

    private void actualizarUsuario() {
        String usr = modView.getTxtUsername();
        String pwd = modView.getTxtPassword();
        if (usr.isEmpty() || pwd.isEmpty()) {
            modView.mostrarMensaje(
                    mensajeHandler.get("usuario.error.campos_obligatorios")
            );
            return;
        }
        Rol rol = Rol.valueOf(modView.getCbxRol().getSelectedItem().toString());
        dao.actualizar(new Usuario(usr, pwd, rol));
        modView.mostrarMensaje(
                mensajeHandler.get("usuario.success.actualizado")
        );
        modView.limpiarCampos();
        refrescar();
    }

    private void buscarEnEliminar() {
        int idx = elimView.getCbxFiltro().getSelectedIndex();
        String txt = elimView.getTxtBuscar().toLowerCase();
        DefaultTableModel m = (DefaultTableModel) elimView.getTblUsuarios().getModel();
        m.setRowCount(0);
        for (Usuario u : dao.listarTodos()) {
            boolean ok = (idx == 1)
                    ? u.getRol().toString().toLowerCase().contains(txt)
                    : u.getUsername().toLowerCase().contains(txt);
            if (ok) m.addRow(new Object[]{u.getUsername(), u.getRol()});
        }
    }

    private void eliminarUsuario() {
        int f = elimView.getTblUsuarios().getSelectedRow();
        if (f < 0) {
            elimView.mostrarMensaje(
                    mensajeHandler.get("usuario.error.campos_obligatorios")
            );
            return;
        }
        String usr = elimView.getModel().getValueAt(f, 0).toString();
        dao.eliminar(usr);
        elimView.mostrarMensaje(
                mensajeHandler.get("usuario.success.eliminado")
        );
        refrescar();
    }

    private void abrirInternal(JInternalFrame v) {
        if (!v.isVisible()) principal.getDesktopPane().add(v);
        v.setVisible(true);
    }

    private void cerrarSesion() {
        principal.dispose();
        loginView.limpiarCampos();
        loginView.setVisible(true);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
