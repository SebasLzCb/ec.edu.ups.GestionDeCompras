package ec.edu.ups.vista.Usuario;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioListaView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JComboBox<String> cbxFiltro;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnRefrescar;
    private JTable tblUsuario;
    private DefaultTableModel model;

    public UsuarioListaView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("usuario.view.listar.titulo"), true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        // Montar UI
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializar modelo y tabla
        model = new DefaultTableModel();
        tblUsuario.setModel(model);

        // Cargar textos y encabezados
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.listar.titulo"));
        btnBuscar.setText(mensajeHandler.get("usuario.view.listar.buscar"));
        btnRefrescar.setText(mensajeHandler.get("usuario.view.listar.refrescar"));

        cbxFiltro.removeAllItems();
        cbxFiltro.addItem(mensajeHandler.get("usuario.view.eliminar.filtro.username"));
        cbxFiltro.addItem(mensajeHandler.get("usuario.view.eliminar.filtro.rol"));

        model.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("usuario.view.listar.col_username"),
                mensajeHandler.get("usuario.view.listar.col_rol"),
                mensajeHandler.get("usuario.view.listar.col_fecha")
        });
    }

    public int getFiltroIndex() {
        return cbxFiltro.getSelectedIndex();
    }

    public String getTxtBuscar() {
        return txtBuscar.getText().trim();
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnRefrescar() {
        return btnRefrescar;
    }

    public JTable getTblUsuario() {
        return tblUsuario;
    }

    public void cargarUsuarios(List<?> usuarios) {
        model.setRowCount(0);
        for (Object u : usuarios) {
            // Suponiendo que u tiene getUsername(), getRol(), getFechaRegistro()
            // Ajustar según modelo Usuario
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
