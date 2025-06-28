package ec.edu.ups.vista.Producto;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoListaView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tblProductos;
    private JPanel panelPrincipal;
    private JButton btnListar;
    private JLabel lblNombre;
    private DefaultTableModel modelo;

    public ProductoListaView(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        modelo = new DefaultTableModel(new Object[]{
                mensajeHandler.get("producto.view.modificar.codigo"),
                mensajeHandler.get("producto.view.modificar.nombre"),
                mensajeHandler.get("producto.view.modificar.precio")
        }, 0);
        tblProductos.setModel(modelo);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("producto.view.listar.titulo"));
        btnBuscar.setText(mensajeHandler.get("producto.view.listar.buscar"));
        btnListar.setText(mensajeHandler.get("producto.view.listar.listar"));
        lblNombre.setText(mensajeHandler.get("producto.view.listar.nombre"));

        modelo.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("producto.view.modificar.codigo"),
                mensajeHandler.get("producto.view.modificar.nombre"),
                mensajeHandler.get("producto.view.modificar.precio")
        });
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JTable getTblProductos() {
        return tblProductos;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }

    public void cargarDatos(List<Producto> listaProductos) {
        if (modelo != null) {
            modelo.setRowCount(0); // Limpia la tabla
            for (Producto producto : listaProductos) {
                modelo.addRow(new Object[]{
                        producto.getCodigo(),
                        producto.getNombre(),
                        producto.getPrecio()
                });
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
