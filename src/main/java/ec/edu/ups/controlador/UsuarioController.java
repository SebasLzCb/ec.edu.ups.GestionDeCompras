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
    private final IRegistroView registroJFrame;
    private final IRegistroView registroJInternalFrame;
    private final UsuarioListaView listView;
    private final UsuarioModView modView;
    private final UsuarioElimView elimView;
    private final Principal principal;
    private final RecuperacionController recuperacionController;
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private Map<String, Pregunta> preguntasMap;

    public UsuarioController(UsuarioDAO dao,
                             LoginView loginView,
                             IRegistroView registroJFrame,
                             IRegistroView registroJInternalFrame,
                             UsuarioListaView listaView,
                             UsuarioModView modView,
                             UsuarioElimView elimView,
                             Principal principal,
                             RecuperacionController recuperacionController,
                             MensajeInternacionalizacionHandler mensajeHandler) {
        this.dao                     = dao;
        this.loginView               = loginView;
        this.registroJFrame          = registroJFrame;
        this.registroJInternalFrame  = registroJInternalFrame;
        this.listView                = listaView;
        this.modView                 = modView;
        this.elimView                = elimView;
        this.principal               = principal;
        this.recuperacionController  = recuperacionController;
        this.mensajeHandler          = mensajeHandler;
        this.preguntasMap            = new HashMap<>();

        configurarVistas();
        configurarEventos();
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    private void configurarVistas() {
        listView.setLocation(40, 40);
        modView.setLocation(60, 60);
        elimView.setLocation(80, 80);

        // El JFrame (registroJFrame) suele posicionarse con setLocationRelativeTo(null) en su constructor
        // o con pack() en la clase principal que lo muestra. No se necesita setLocation aquí.

        // CAMBIO: Se realiza un 'cast' a JInternalFrame para poder usar setLocation
        if (registroJInternalFrame instanceof JInternalFrame) {
            ((JInternalFrame) registroJInternalFrame).setLocation(100, 100);
        } else {
            System.err.println("ADVERTENCIA: registroJInternalFrame no es una instancia de JInternalFrame.");
        }
    }

    private void configurarEventos() {
        System.out.println("DEBUG: Intentando configurar eventos de RegistroView.");
        if (registroJFrame == null) {
            System.err.println("ERROR DEBUG: registroJFrame es NULL en configurarEventos. Los listeners NO se adjuntarán.");
            // No retornamos aquí, para que intente adjuntar listeners al JInternalFrame si existe.
        } else {
            if (registroJFrame.getBtnCrear() == null) {
                System.err.println("ERROR DEBUG: registroJFrame.getBtnCrear() devuelve NULL.");
            }
            if (registroJFrame.getBtnCancelar() == null) {
                System.err.println("ERROR DEBUG: registroJFrame.getBtnCancelar() devuelve NULL.");
            }
        }

        if (registroJInternalFrame == null) {
            System.err.println("ERROR DEBUG: registroJInternalFrame es NULL en configurarEventos. Los listeners NO se adjuntarán.");
            // No retornamos aquí, para que los listeners del JFrame puedan adjuntarse si existen.
        } else {
            if (registroJInternalFrame.getBtnCrear() == null) {
                System.err.println("ERROR DEBUG: registroJInternalFrame.getBtnCrear() devuelve NULL.");
            }
            if (registroJInternalFrame.getBtnCancelar() == null) {
                System.err.println("ERROR DEBUG: registroJInternalFrame.getBtnCancelar() devuelve NULL.");
            }
        }

        loginView.getBtnRegistrar().addActionListener(e -> {
            if (registroJFrame != null) {
                List<String> preguntas = recuperacionController.obtenerPreguntasLocalizadas();
                registroJFrame.setPreguntas(preguntas);
                registroJFrame.limpiarCampos();
                registroJFrame.setVisible(true);
            } else {
                loginView.mostrarMensaje("Error: La ventana de registro (JFrame) no fue inicializada.");
            }
        });

        // Listeners para los botones del JFrame de registro (RegistroView)
        if (registroJFrame != null && registroJFrame.getBtnCrear() != null && registroJFrame.getBtnCancelar() != null) {
            System.out.println("DEBUG: Adjuntando listeners a RegistroView (JFrame).");
            registroJFrame.getBtnCrear().addActionListener(e -> registrarUsuario(registroJFrame));
            registroJFrame.getBtnCancelar().addActionListener(e -> registroJFrame.dispose());
        }

        // Listeners para los botones del JInternalFrame de registro (UsuarioRegistroView)
        if (registroJInternalFrame != null && registroJInternalFrame.getBtnCrear() != null && registroJInternalFrame.getBtnCancelar() != null) {
            System.out.println("DEBUG: Adjuntando listeners a UsuarioRegistroView (JInternalFrame).");
            registroJInternalFrame.getBtnCrear().addActionListener(e -> registrarUsuario(registroJInternalFrame));
            registroJInternalFrame.getBtnCancelar().addActionListener(e -> registroJInternalFrame.dispose());
        }

        listView.getBtnRefrescar().addActionListener(e -> refrescar());
        listView.getBtnBuscar().addActionListener(e -> buscarEnLista());

        principal.getMenuItemModificarUsuario().addActionListener(e -> {
            refrescar();
            abrirInternal(modView);
        });
        modView.getBtnBuscar().addActionListener(e -> refrescar());
        modView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        modView.getBtnCancelar().addActionListener(e -> modView.dispose());

        // Listener para el menú "Registrar Usuario" (abre el JInternalFrame)
        principal.getMenuItemRegistrarUsuario().addActionListener(ev -> {
            if (registroJInternalFrame != null) {
                List<Pregunta> preguntas = recuperacionController.getRecuperacionDAO().getBancoPreguntas();
                preguntasMap.clear();
                for (Pregunta p : preguntas) {
                    preguntasMap.put(mensajeHandler.get("recuperacion.pregunta." + p.getId()), p);
                }
                registroJInternalFrame.setPreguntas(preguntasMap.keySet().stream().collect(Collectors.toList()));
                registroJInternalFrame.limpiarCampos();
                abrirInternal((JInternalFrame) registroJInternalFrame); // Se asegura de que sea un JInternalFrame
            } else {
                principal.mostrarMensaje("Error: La ventana de registro interna no fue inicializada.");
            }
        });

        principal.getMenuItemListarUsuarios().addActionListener(e -> {
            refrescar();
            abrirInternal(listView);
        });
        principal.getMenuItemCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void registrarUsuario(IRegistroView currentView) {
        System.out.println("DEBUG: Método registrarUsuario() inicia para la vista: " + currentView.getClass().getSimpleName());
        String username     = currentView.getTxtUsuario();
        String password     = currentView.getTxtPassword();
        String nombresComp  = currentView.getTxtNombresCompletos();
        String correo       = currentView.getTxtCorreo();
        String telefono     = currentView.getTxtTelefono();
        LocalDate fechaNac  = currentView.getFechaNacimiento();
        String respuesta1   = currentView.getTxtRespuesta1();
        String respuesta2   = currentView.getTxtRespuesta2();
        String respuesta3   = currentView.getTxtRespuesta3();

        String q1 = (String) currentView.getCbxPregunta1().getSelectedItem();
        String q2 = (String) currentView.getCbxPregunta2().getSelectedItem();
        String q3 = (String) currentView.getCbxPregunta3().getSelectedItem();

        try {
            ValidacionUtils.validarCampoObligatorio(username, mensajeHandler.get("usuario.view.registrar.username"));
            ValidacionUtils.validarCampoObligatorio(password, mensajeHandler.get("usuario.view.registrar.password"));
            ValidacionUtils.validarCampoObligatorio(nombresComp, mensajeHandler.get("usuario.view.registrar.nombres"));
            ValidacionUtils.validarCampoObligatorio(correo, mensajeHandler.get("usuario.view.registrar.correo"));
            ValidacionUtils.validarCampoObligatorio(telefono, mensajeHandler.get("usuario.view.registrar.telefono"));
            ValidacionUtils.validarCampoObligatorio(respuesta1, mensajeHandler.get("usuario.view.registrar.respuesta1"));
            ValidacionUtils.validarCampoObligatorio(respuesta2, mensajeHandler.get("usuario.view.registrar.respuesta2"));
            ValidacionUtils.validarCampoObligatorio(respuesta3, mensajeHandler.get("usuario.view.registrar.respuesta3"));

            ValidacionUtils.validarCedulaEcuatoriana(username);
            ValidacionUtils.validarContrasenia(password);
            ValidacionUtils.validarCorreo(correo);

            if (fechaNac == null) {
                throw new ValidacionException(mensajeHandler.get("usuario.error.registro.fecha_nacimiento_invalida"));
            }
            if (fechaNac.isAfter(LocalDate.now())) {
                throw new ValidacionException(mensajeHandler.get("usuario.error.registro.fecha_futura"));
            }

            if (currentView.getCbxPregunta1().getSelectedIndex() <= 0 ||
                    currentView.getCbxPregunta2().getSelectedIndex() <= 0 ||
                    currentView.getCbxPregunta3().getSelectedIndex() <= 0) {
                throw new ValidacionException(mensajeHandler.get("usuario.error.registro.seleccionar_preguntas"));
            }
            if (q1.equals(q2) || q1.equals(q3) || q2.equals(q3)) {
                throw new ValidacionException(mensajeHandler.get("usuario.error.registro.preguntas_diferentes"));
            }

            Usuario nuevoUsuario = new Usuario(
                    username,
                    password,
                    Rol.USUARIO,
                    nombresComp,
                    fechaNac,
                    correo,
                    telefono
            );

            Pregunta p1 = recuperacionController.getRecuperacionDAO().getBancoPreguntas().stream().filter(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()).equals(q1)).findFirst().orElse(null);
            Pregunta p2 = recuperacionController.getRecuperacionDAO().getBancoPreguntas().stream().filter(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()).equals(q2)).findFirst().orElse(null);
            Pregunta p3 = recuperacionController.getRecuperacionDAO().getBancoPreguntas().stream().filter(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()).equals(q3)).findFirst().orElse(null);

            if (p1 == null || p2 == null || p3 == null) {
                throw new ValidacionException(mensajeHandler.get("usuario.error.registro.preguntas_no_encontradas"));
            }

            List<Respuesta> respuestas = List.of(
                    new Respuesta(nuevoUsuario, p1, respuesta1),
                    new Respuesta(nuevoUsuario, p2, respuesta2),
                    new Respuesta(nuevoUsuario, p3, respuesta3)
            );

            if (dao.buscarPorUsername(username) != null) {
                throw new ValidacionException(mensajeHandler.get("usuario.error.registro.username_existe"));
            }

            dao.crear(nuevoUsuario);
            recuperacionController.registrarPreguntas(nuevoUsuario, respuestas);

            currentView.mostrarMensaje(mensajeHandler.get("usuario.success.registro_exitoso"));
            currentView.limpiarCampos();
            currentView.dispose();
            refrescar();

        } catch (ValidacionException exc) {
            currentView.mostrarMensaje(exc.getMessage());
        } catch (Exception exc) {
            System.err.println("Error inesperado al registrar usuario: " + exc.getMessage());
            exc.printStackTrace();
            currentView.mostrarMensaje(mensajeHandler.get("usuario.error.registro.inesperado"));
        }
        System.out.println("DEBUG: Método registrarUsuario() finaliza.");
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
            try {
                ValidacionUtils.validarContrasenia(nuevoPassword);
                usuarioParaActualizar.setContrasenia(nuevoPassword);
            } catch (ValidacionException e) {
                modView.mostrarMensaje(e.getMessage());
                return;
            }
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
                    mensajeHandler.get("usuario.error.seleccione_usuario")
            );
            return;
        }
        String usr = elimView.getModel().getValueAt(f, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(elimView,
                mensajeHandler.get("usuario.confirm.eliminar") + " " + usr + "?",
                mensajeHandler.get("usuario.confirm.titulo"),
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.eliminar(usr);
            elimView.mostrarMensaje(
                    mensajeHandler.get("usuario.success.eliminado")
            );
            refrescar();
        }
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