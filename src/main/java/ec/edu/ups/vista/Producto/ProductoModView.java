package ec.edu.ups.vista.Producto;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoModView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private JTable tblProductos;
    private DefaultTableModel modelo;

    public ProductoModView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("producto.view.modificar.titulo"), true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        // Monta el panel generado por el .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializa modelo para la tabla
        modelo = new DefaultTableModel();
        tblProductos.setModel(modelo);

        // Carga textos y encabezados según idioma
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("producto.view.modificar.titulo"));

        lblCodigo    .setText(mensajeHandler.get("producto.view.modificar.codigo") + ":");
        lblNombre    .setText(mensajeHandler.get("producto.view.modificar.nombre") + ":");
        lblPrecio    .setText(mensajeHandler.get("producto.view.modificar.precio") + ":");

        btnBuscar    .setText(mensajeHandler.get("producto.view.modificar.buscar"));
        btnActualizar.setText(mensajeHandler.get("producto.view.modificar.actualizar"));
        btnCancelar  .setText(mensajeHandler.get("producto.view.modificar.cancelar"));

        modelo.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("producto.view.modificar.codigo"),
                mensajeHandler.get("producto.view.modificar.nombre"),
                mensajeHandler.get("producto.view.modificar.precio")
        });
    }

    public String getTxtCodigo()    { return txtCodigo.getText().trim(); }
    public JButton getBtnBuscar()   { return btnBuscar; }
    public JButton getBtnActualizar(){ return btnActualizar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JTable getTblProductos() { return tblProductos; }

    public void listarProductos(List<Producto> productos) {
        modelo.setRowCount(0);
        for (Producto p : productos) {
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

    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
    }
}
