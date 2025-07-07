package ec.edu.ups.vista.Usuario;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UsuarioElimView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JComboBox<String> cbxFiltro;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JTable tblUsuarios;
    private DefaultTableModel model;

    public UsuarioElimView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("usuario.view.eliminar.titulo"), true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        // Monta el panel generado por el .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializa modelo y tabla
        model = new DefaultTableModel();
        tblUsuarios.setModel(model);

        // Carga textos según el idioma actual
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.eliminar.titulo"));
        btnBuscar .setText(mensajeHandler.get("usuario.view.eliminar.buscar"));
        btnEliminar.setText(mensajeHandler.get("usuario.view.eliminar.eliminar"));

        cbxFiltro.removeAllItems();
        cbxFiltro.addItem(mensajeHandler.get("usuario.view.eliminar.filtro.username"));
        cbxFiltro.addItem(mensajeHandler.get("usuario.view.eliminar.filtro.rol"));

        model.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("usuario.view.eliminar.col_username"),
                mensajeHandler.get("usuario.view.eliminar.col_rol")
        });
    }

    public JComboBox<String> getCbxFiltro() {
        return cbxFiltro;
    }

    public String getTxtBuscar() {
        return txtBuscar.getText().trim();
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JTable getTblUsuarios() {
        return tblUsuarios;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
