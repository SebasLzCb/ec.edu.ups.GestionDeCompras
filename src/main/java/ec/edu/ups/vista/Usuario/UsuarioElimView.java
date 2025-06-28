package ec.edu.ups.vista.Usuario;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UsuarioElimView extends JInternalFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JComboBox<String> cbxFiltro;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tblUsuarios;
    private JButton btnEliminar;
    private DefaultTableModel model;

    public UsuarioElimView(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        model = new DefaultTableModel(new Object[]{"Username", "Rol"}, 0);
        tblUsuarios.setModel(model);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.eliminar.titulo"));
        btnBuscar.setText(mensajeHandler.get("usuario.view.eliminar.buscar"));
        btnEliminar.setText(mensajeHandler.get("usuario.view.eliminar.eliminar"));

        cbxFiltro.removeAllItems();
        cbxFiltro.addItem(mensajeHandler.get("usuario.view.eliminar.filtro.username"));
        cbxFiltro.addItem(mensajeHandler.get("usuario.view.eliminar.filtro.rol"));
    }

    public JTable getTableUsuarios() {
        return tblUsuarios;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JComboBox<String> getCbxFiltro() {
        return cbxFiltro;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setTableModel(DefaultTableModel model) {
        this.model = model;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
