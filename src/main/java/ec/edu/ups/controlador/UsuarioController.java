package ec.edu.ups.controlador;

import ec.edu.ups.Main;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.InicioDeSesion.LoginView;
import ec.edu.ups.vista.InicioDeSesion.RegistroView;
import ec.edu.ups.vista.Principal;
import ec.edu.ups.vista.Usuario.UsuarioRegistroView;
import ec.edu.ups.vista.Usuario.UsuarioListaView;
import ec.edu.ups.vista.Usuario.UsuarioModView;
import ec.edu.ups.vista.Usuario.UsuarioElimView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private Map<String, Pregunta> preguntasMap;

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
        this.preguntasMap            = new HashMap<>();

        configurarVistas();
        configurarEventos();
    }

    private void configurarVistas() {
        listView.setLocation(40, 40);
        modView.setLocation(60, 60);
        elimView.setLocation(80, 80);
        regView.setLocation(100, 100);
    }

    private void configurarEventos() {
        loginView.getBtnIniciarSesion().addActionListener(e -> autenticar());
        loginView.getBtnRegistrar().addActionListener(e -> {
            RegistroView registroJFrame = loginView.getRegistroFrame();
            if (registroJFrame != null) {
                List<String> preguntas = recuperacionController.obtenerPreguntasLocalizadas();
                registroJFrame.setPreguntas(preguntas);
                registroJFrame.limpiarCampos();
                registroJFrame.setVisible(true);
            }
        });

        regView.getBtnCrear().addActionListener(e -> registrar());
        regView.getBtnCancelar().addActionListener(e -> regView.dispose());

        listView.getBtnRefrescar().addActionListener(e -> refrescar());
        listView.getBtnBuscar().addActionListener(e -> buscarEnLista());

        principal.getMenuItemModificarUsuario().addActionListener(e -> {
            refrescar();
            abrirInternal(modView);
        });
        modView.getBtnBuscar().addActionListener(e -> refrescar());
        modView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        modView.getBtnCancelar().addActionListener(e -> modView.dispose());

        principal.getMenuItemEliminarUsuario().addActionListener(e -> {
            refrescar();
            abrirInternal(elimView);
        });
        elimView.getBtnBuscar().addActionListener(e -> buscarEnEliminar());
        elimView.getBtnEliminar().addActionListener(e -> eliminarUsuario());

        principal.getMenuItemRegistrarUsuario().addActionListener(ev -> {
            List<Pregunta> preguntas = recuperacionController.getRecuperacionDAO().getBancoPreguntas();
            preguntasMap.clear();
            for (Pregunta p : preguntas) {
                preguntasMap.put(mensajeHandler.get("recuperacion.pregunta." + p.getId()), p);
            }
            regView.setPreguntas(preguntasMap.keySet().stream().collect(Collectors.toList()));
            abrirInternal(regView);
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

        if (regView.getTxtUsuario().isEmpty() || regView.getTxtPassword().isEmpty() || regView.getTxtNombresCompletos().isEmpty() || regView.getTxtCorreo().isEmpty()) {
            regView.mostrarMensaje(mensajeHandler.get("usuario.error.registro.campos_obligatorios"));
            return;
        }

        if (regView.getCbxPregunta1().getSelectedIndex() <= 0 || regView.getCbxPregunta2().getSelectedIndex() <= 0 || regView.getCbxPregunta3().getSelectedIndex() <= 0) {
            regView.mostrarMensaje(mensajeHandler.get("usuario.error.registro.seleccionar_preguntas"));
            return;
        }

        if (regView.getTxtRespuesta1().isEmpty() || regView.getTxtRespuesta2().isEmpty() || regView.getTxtRespuesta3().isEmpty()) {
            regView.mostrarMensaje(mensajeHandler.get("usuario.error.registro.responder_preguntas"));
            return;
        }

        String q1 = regView.getCbxPregunta1().getSelectedItem().toString();
        String q2 = regView.getCbxPregunta2().getSelectedItem().toString();
        String q3 = regView.getCbxPregunta3().getSelectedItem().toString();

        if (q1.equals(q2) || q1.equals(q3) || q2.equals(q3)) {
            regView.mostrarMensaje(mensajeHandler.get("usuario.error.registro.preguntas_diferentes"));
            return;
        }

        Usuario nuevoUsuario = new Usuario(
                regView.getTxtUsuario(),
                regView.getTxtPassword(),
                Rol.USUARIO,
                regView.getTxtNombresCompletos(),
                regView.getFechaNacimiento(),
                regView.getTxtCorreo(),
                regView.getTxtTelefono()
        );

        Pregunta p1 = preguntasMap.get(q1);
        Pregunta p2 = preguntasMap.get(q2);
        Pregunta p3 = preguntasMap.get(q3);

        List<Respuesta> respuestas = List.of(
                new Respuesta(nuevoUsuario, p1, regView.getTxtRespuesta1()),
                new Respuesta(nuevoUsuario, p2, regView.getTxtRespuesta2()),
                new Respuesta(nuevoUsuario, p3, regView.getTxtRespuesta3())
        );

        dao.crear(nuevoUsuario);
        recuperacionController.registrarPreguntas(nuevoUsuario, respuestas);

        regView.mostrarMensaje(mensajeHandler.get("usuario.success.registro_exitoso"));
        regView.limpiarCampos();
        regView.dispose();
        refrescar();
    }

    private void refrescar() {
        DefaultTableModel mList = (DefaultTableModel) listView.getTblUsuario().getModel();
        DefaultTableModel mElim = (DefaultTableModel) elimView.getTblUsuarios().getModel();
        DefaultTableModel mMod = (DefaultTableModel) modView.getTblUsuarios().getModel();

        mList.setRowCount(0);
        mElim.setRowCount(0);
        mMod.setRowCount(0);

        for (Usuario u : dao.listarTodos()) {
            Object[] fila = {
                    u.getUsername(),
                    u.getRol(),
                    (u.getFechaNacimiento() != null) ? u.getFechaNacimiento().toString() : "No especificada"
            };
            mList.addRow(fila);

            Object[] filaCorta = {u.getUsername(), u.getRol()};
            mElim.addRow(filaCorta);
            mMod.addRow(filaCorta);
        }
    }

    private void buscarEnLista() {
        String txt = listView.getTxtBuscar().toLowerCase();
        int idx = listView.getFiltroIndex();
        DefaultTableModel m = (DefaultTableModel) listView.getTblUsuario().getModel();
        m.setRowCount(0);
        for (Usuario u : dao.listarTodos()) {
            boolean ok = (idx == 1)
                    ? u.getRol().toString().toLowerCase().contains(txt)
                    : u.getUsername().toLowerCase().contains(txt);
            if (ok) {
                Object[] fila = {
                        u.getUsername(),
                        u.getRol(),
                        (u.getFechaNacimiento() != null) ? u.getFechaNacimiento().toString() : "No especificada"
                };
                m.addRow(fila);
            }
        }
    }

    private void actualizarUsuario() {
        int filaSeleccionada = modView.getTblUsuarios().getSelectedRow();
        if (filaSeleccionada < 0) {
            modView.mostrarMensaje("Por favor, seleccione un usuario de la tabla para actualizar.");
            return;
        }

        String username = modView.getTblUsuarios().getValueAt(filaSeleccionada, 0).toString();
        Usuario usuarioParaActualizar = dao.buscarPorUsername(username);

        if (usuarioParaActualizar == null) {
            modView.mostrarMensaje("El usuario seleccionado ya no existe.");
            refrescar();
            return;
        }

        String nuevoPassword = modView.getTxtPassword();
        if (!nuevoPassword.isEmpty()) {
            usuarioParaActualizar.setContrasenia(nuevoPassword);
        }

        String rolSeleccionado = (String) modView.getCbxRol().getSelectedItem();
        if (rolSeleccionado.equals(mensajeHandler.get("usuario.view.modificar.rol.admin"))) {
            usuarioParaActualizar.setRol(Rol.ADMINISTRADOR);
        } else {
            usuarioParaActualizar.setRol(Rol.USUARIO);
        }

        dao.actualizar(usuarioParaActualizar);
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
        if (!v.isVisible()) {
            if (v.getParent() == null) {
                principal.getDesktopPane().add(v);
            }
            v.setVisible(true);
        }
        try {
            v.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        v.toFront();
    }

    private void cerrarSesion() {
        usuarioActual = null;
        principal.setVisible(false);
        for (JInternalFrame frame : principal.getDesktopPane().getAllFrames()) {
            frame.dispose();
        }
        loginView.limpiarCampos();
        loginView.setVisible(true);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}