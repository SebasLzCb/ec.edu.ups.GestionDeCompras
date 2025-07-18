package ec.edu.ups.modelo;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public interface IRegistroView {

    String getTxtUsuario();
    String getTxtPassword();
    String getTxtNombresCompletos();
    String getTxtCorreo();
    String getTxtTelefono();

    LocalDate getFechaNacimiento();

    JComboBox<String> getCbxPregunta1();
    JComboBox<String> getCbxPregunta2();
    JComboBox<String> getCbxPregunta3();

    String getTxtRespuesta1();
    String getTxtRespuesta2();
    String getTxtRespuesta3();

    JButton getBtnCrear();
    JButton getBtnCancelar();

    void setPreguntas(List<String> preguntas);
    void actualizarIdioma();
    void limpiarCampos();
    void mostrarMensaje(String mensaje);
    void dispose();
    void setVisible(boolean visible);

}