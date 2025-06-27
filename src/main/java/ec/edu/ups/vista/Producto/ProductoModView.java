package ec.edu.ups.vista.Producto;

import ec.edu.ups.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoModView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private JTable tblProductos;
    private DefaultTableModel modelo;

    public ProductoModView() {
        setContentPane(panelPrincipal);
        setTitle("Modificar Producto");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        modelo = new DefaultTableModel(new Object[]{"Código", "Nombre", "Precio"}, 0);
        tblProductos.setModel(modelo);
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JTable getTableProductos() {
        return tblProductos;
    }

    public DefaultTableModel getTableModel() {
        return modelo;
    }

    public void listarProductos(List<Producto> productos) {
        modelo.setRowCount(0);
        for (Producto p : productos) {
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
    }
}
