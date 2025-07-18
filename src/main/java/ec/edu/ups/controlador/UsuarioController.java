package ec.edu.ups.controlador;

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
import ec.edu.ups.util.ValidacionUtils;
import ec.edu.ups.excepciones.ValidacionException;
import ec.edu.ups.modelo.IRegistroView;
import ec.edu.ups.dao.DAOManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsuarioController {

    private Usuario usuarioActual;
    private UsuarioDAO usuarioDAO;
    private final LoginView loginView;
    private final UsuarioRegistroView regViewInternal;
    private final UsuarioListaView listView;
    private final UsuarioModView modView;
    private final UsuarioElimView elimView;
    private final Principal principal;
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private RecuperacionController recuperacionController;
    private Map<String, Pregunta> preguntasMap;

    private IRegistroView registroFrame;
    private final DAOManager daoManager;

    public UsuarioController(UsuarioDAO usuarioDAO,
                             LoginView loginView,
                             UsuarioRegistroView regViewInternal,
                             UsuarioListaView listView,
                             UsuarioModView modView,
                             UsuarioElimView elimView,
                             Principal principal,
                             RecuperacionController recuperacionController,
                             MensajeInternacionalizacionHandler mensajeHandler,
                             DAOManager daoManager) {
        this.usuarioDAO = usuarioDAO;
        this.loginView = loginView;
        this.regViewInternal = regViewInternal;
        this.listView = listView;
        this.modView = modView;
        this.elimView = elimView;
        this.principal = principal;
        this.recuperacionController = recuperacionController;
        this.mensajeHandler = mensajeHandler;
        this.preguntasMap = new HashMap<>();
        this.daoManager = daoManager;

        configurarVistas();
        configurarEventos();
    }

    public void setRegistroFrame(IRegistroView registroFrame) {
        this.registroFrame = registroFrame;
        this.registroFrame.getBtnCrear().addActionListener(e -> registrarUsuario(this.registroFrame));
        this.registroFrame.getBtnCancelar().addActionListener(e -> this.registroFrame.dispose());
    }

    private void configurarVistas() {
        regViewInternal.setLocation(100, 100);
        listView.setLocation(40, 40);
        modView.setLocation(60, 60);
        elimView.setLocation(80, 80);
    }

    private void configurarEventos() {
        loginView.getBtnIniciarSesion().addActionListener(e -> autenticar());

        loginView.getBtnRegistrar().addActionListener(e -> {
            if (registroFrame != null) {
                List<String> preguntas = recuperacionController.obtenerPreguntasLocalizadas();
                registroFrame.setPreguntas(preguntas);
                registroFrame.limpiarCampos();
                registroFrame.setVisible(true);
            } else {
                loginView.mostrarMensaje(mensajeHandler.get("error.registro_frame_no_inicializado"));
            }
        });

        regViewInternal.getBtnCrear().addActionListener(e -> registrarUsuario(regViewInternal));
        regViewInternal.getBtnCancelar().addActionListener(e -> regViewInternal.dispose());

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
            regViewInternal.setPreguntas(preguntasMap.keySet().stream().collect(Collectors.toList()));
            regViewInternal.limpiarCampos();
            abrirInternal(regViewInternal);
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
        String storageTypeKey = loginView.getSelectedStorageTypeKey();

        // CORRECCIÓN: Obtener la ruta de archivos directamente de LoginView
        String filePath = loginView.getRutaArchivos();

        daoManager.initializeDAOs(storageTypeKey, filePath);

        this.usuarioDAO = daoManager.getUsuarioDAO();
        recuperacionController.setRecuperacionDAO(daoManager.getRecuperacionDAO());
        recuperacionController.setUsuarioDAO(daoManager.getUsuarioDAO());

        Usuario authenticatedUser = usuarioDAO.autenticar(usr, pwd);

        if (authenticatedUser == null) {
            loginView.mostrarMensaje(mensajeHandler.get("usuario.error.credenciales"));
        } else {
            usuarioActual = authenticatedUser;
            loginView.dispose();
            principal.setVisible(true);
        }
    }

    public void registrarUsuario(IRegistroView view) {
        try {
            String username = view.getTxtUsuario();
            String password = view.getTxtPassword();
            String nombresCompletos = view.getTxtNombresCompletos();
            LocalDate fechaNacimiento = view.getFechaNacimiento();
            String correo = view.getTxtCorreo();
            String telefono = view.getTxtTelefono();

            ValidacionUtils.validarCampoObligatorio(username, mensajeHandler.get("usuario.view.registrar.username"));
            ValidacionUtils.validarCampoObligatorio(password, mensajeHandler.get("usuario.view.registrar.password"));
            ValidacionUtils.validarCampoObligatorio(nombresCompletos, mensajeHandler.get("usuario.view.registrar.nombres"));
            ValidacionUtils.validarCampoObligatorio(correo, mensajeHandler.get("usuario.view.registrar.correo"));
            ValidacionUtils.validarCampoObligatorio(telefono, mensajeHandler.get("usuario.view.registrar.telefono"));

            ValidacionUtils.validarCedulaEcuatoriana(username);
            ValidacionUtils.validarContrasenia(password);
            ValidacionUtils.validarCorreo(correo);

            if (fechaNacimiento == null) {
                throw new ValidacionException("validacion.error.fecha_nacimiento_invalida");
            }

            if (usuarioDAO.buscarPorUsername(username) != null) {
                throw new ValidacionException("usuario.error.username_ya_existe");
            }

            String selectedQ1 = (String) view.getCbxPregunta1().getSelectedItem();
            String selectedQ2 = (String) view.getCbxPregunta2().getSelectedItem();
            String selectedQ3 = (String) view.getCbxPregunta3().getSelectedItem();

            if (selectedQ1 == null || selectedQ1.equals(mensajeHandler.get("usuario.view.registrar.seleccione_pregunta")) ||
                    selectedQ2 == null || selectedQ2.equals(mensajeHandler.get("usuario.view.registrar.seleccione_pregunta")) ||
                    selectedQ3 == null || selectedQ3.equals(mensajeHandler.get("usuario.view.registrar.seleccione_pregunta"))) {
                throw new ValidacionException("usuario.error.registro.seleccionar_preguntas");
            }

            if (view.getTxtRespuesta1().isEmpty() ||
                    view.getTxtRespuesta2().isEmpty() ||
                    view.getTxtRespuesta3().isEmpty()) {
                throw new ValidacionException("usuario.error.registro.responder_preguntas");
            }

            if (selectedQ1.equals(selectedQ2) || selectedQ1.equals(selectedQ3) || selectedQ2.equals(selectedQ3)) {
                throw new ValidacionException("usuario.error.registro.preguntas_diferentes");
            }

            Usuario nuevoUsuario = new Usuario(
                    username,
                    password,
                    Rol.USUARIO,
                    nombresCompletos,
                    fechaNacimiento,
                    correo,
                    telefono
            );

            Pregunta p1 = recuperacionController.getRecuperacionDAO().getBancoPreguntas().stream()
                    .filter(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()).equals(selectedQ1))
                    .findFirst()
                    .orElseThrow(() -> new ValidacionException("error.pregunta_no_encontrada"));

            Pregunta p2 = recuperacionController.getRecuperacionDAO().getBancoPreguntas().stream()
                    .filter(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()).equals(selectedQ2))
                    .findFirst()
                    .orElseThrow(() -> new ValidacionException("error.pregunta_no_encontrada"));

            Pregunta p3 = recuperacionController.getRecuperacionDAO().getBancoPreguntas().stream()
                    .filter(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()).equals(selectedQ3))
                    .findFirst()
                    .orElseThrow(() -> new ValidacionException("error.pregunta_no_encontrada"));

            List<Respuesta> respuestas = List.of(
                    new Respuesta(nuevoUsuario, p1, view.getTxtRespuesta1()),
                    new Respuesta(nuevoUsuario, p2, view.getTxtRespuesta2()),
                    new Respuesta(nuevoUsuario, p3, view.getTxtRespuesta3())
            );

            usuarioDAO.crear(nuevoUsuario);
            recuperacionController.registrarPreguntas(nuevoUsuario, respuestas);

            view.mostrarMensaje(mensajeHandler.get("usuario.success.registro_exitoso"));
            view.limpiarCampos();
            view.dispose();
            refrescar();

        } catch (ValidacionException e) {
            view.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (Exception e) {
            view.mostrarMensaje(mensajeHandler.get("error.inesperado") + ": " + e.getMessage());
        }
    }

    private void refrescar() {
        DefaultTableModel mList = (DefaultTableModel) listView.getTblUsuario().getModel();
        DefaultTableModel mElim = (DefaultTableModel) elimView.getTblUsuarios().getModel();
        DefaultTableModel mMod = (DefaultTableModel) modView.getTblUsuarios().getModel();

        mList.setRowCount(0);
        mElim.setRowCount(0);
        mMod.setRowCount(0);

        for (Usuario u : usuarioDAO.listarTodos()) {
            Object[] fila = {
                    u.getUsername(),
                    u.getRol(),
                    (u.getFechaNacimiento() != null) ? u.getFechaNacimiento().toString() : mensajeHandler.get("usuario.fecha_no_especificada")
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
        for (Usuario u : usuarioDAO.listarTodos()) {
            boolean ok = (idx == 1)
                    ? u.getRol().toString().toLowerCase().contains(txt)
                    : u.getUsername().toLowerCase().contains(txt);
            if (ok) {
                Object[] fila = {
                        u.getUsername(),
                        u.getRol(),
                        (u.getFechaNacimiento() != null) ? u.getFechaNacimiento().toString() : mensajeHandler.get("usuario.fecha_no_especificada")
                };
                m.addRow(fila);
            }
        }
    }

    private void actualizarUsuario() {
        int filaSeleccionada = modView.getTblUsuarios().getSelectedRow();
        if (filaSeleccionada < 0) {
            modView.mostrarMensaje(mensajeHandler.get("usuario.error.seleccione_usuario_modificar"));
            return;
        }

        String username = modView.getTblUsuarios().getValueAt(filaSeleccionada, 0).toString();
        Usuario usuarioParaActualizar = usuarioDAO.buscarPorUsername(username);

        if (usuarioParaActualizar == null) {
            modView.mostrarMensaje(mensajeHandler.get("usuario.error.usuario_no_existe"));
            refrescar();
            return;
        }

        String nuevoPassword = modView.getTxtPassword();
        try {
            if (!nuevoPassword.isEmpty()) {
                ValidacionUtils.validarContrasenia(nuevoPassword);
                usuarioParaActualizar.setContrasenia(nuevoPassword);
            }
        } catch (ValidacionException e) {
            modView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
            return;
        }


        String rolSeleccionadoKey = (String) modView.getCbxRol().getSelectedItem();
        Rol nuevoRol;
        if (rolSeleccionadoKey.equals(mensajeHandler.get("usuario.view.modificar.rol.admin"))) {
            nuevoRol = Rol.ADMINISTRADOR;
        } else if (rolSeleccionadoKey.equals(mensajeHandler.get("usuario.view.modificar.rol.usuario"))) {
            nuevoRol = Rol.USUARIO;
        } else {
            modView.mostrarMensaje(mensajeHandler.get("usuario.error.rol_invalido"));
            return;
        }
        usuarioParaActualizar.setRol(nuevoRol);


        usuarioDAO.actualizar(usuarioParaActualizar);
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
        for (Usuario u : usuarioDAO.listarTodos()) {
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
                    mensajeHandler.get("usuario.error.seleccione_usuario_eliminar")
            );
            return;
        }
        String usr = elimView.getModel().getValueAt(f, 0).toString();

        if (usr.equals("admin")) {
            elimView.mostrarMensaje(mensajeHandler.get("usuario.error.no_eliminar_admin"));
            return;
        }

        usuarioDAO.eliminar(usr);
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
            // Ignored
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