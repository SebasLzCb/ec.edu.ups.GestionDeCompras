package ec.edu.ups.vista.Producto;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoListaView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;
    private JPanel panelPrincipal;
    private JLabel lblNombre;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnListar;
    private JTable tblProductos;
    private DefaultTableModel modelo;

    public ProductoListaView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("producto.view.listar.titulo"), true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        // Monta el panel generado por el .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializa modelo y tabla
        modelo = new DefaultTableModel();
        tblProductos.setModel(modelo);

        // Carga textos y encabezados
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("producto.view.listar.titulo"));
        lblNombre.setText(mensajeHandler.get("producto.view.listar.nombre") + ":");
        btnBuscar.setText(mensajeHandler.get("producto.view.listar.buscar"));
        btnListar.setText(mensajeHandler.get("producto.view.listar.listar"));

        modelo.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("producto.view.modificar.codigo"),
                mensajeHandler.get("producto.view.modificar.nombre"),
                mensajeHandler.get("producto.view.modificar.precio")
        });
    }

    public String getTxtBuscar() {
        return txtBuscar.getText().trim();
    }

    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnListar() { return btnListar; }
    public JTable getTblProductos() { return tblProductos; }

    public void cargarDatos(List<Producto> listaProductos) {
        modelo.setRowCount(0);
        for (Producto p : listaProductos) {
            modelo.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getPrecio()
            });
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
