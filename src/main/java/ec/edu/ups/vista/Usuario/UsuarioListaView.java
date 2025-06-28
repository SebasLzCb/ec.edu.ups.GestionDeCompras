package ec.edu.ups.vista.Usuario;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UsuarioListaView extends JInternalFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTable tblUsuario;
    private DefaultTableModel model;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnRefrescar;
    private JComboBox<String> cbxFiltro;

    public UsuarioListaView(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        model = new DefaultTableModel(new Object[]{"Username", "Rol"}, 0);
        tblUsuario.setModel(model);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.listar.titulo"));
        btnBuscar.setText(mensajeHandler.get("usuario.view.listar.buscar"));
        btnRefrescar.setText(mensajeHandler.get("usuario.view.listar.refrescar"));

        cbxFiltro.removeAllItems();
        cbxFiltro.addItem(mensajeHandler.get("usuario.view.eliminar.filtro.username"));
        cbxFiltro.addItem(mensajeHandler.get("usuario.view.eliminar.filtro.rol"));
    }

    public JComboBox<String> getComboFiltro() {
        return cbxFiltro;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnRefrescar() {
        return btnRefrescar;
    }

    public JTable getTableUsuarios() {
        return tblUsuario;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }

    public void setTableModel(DefaultTableModel model) {
        this.model = model;
        tblUsuario.setModel(model);
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
