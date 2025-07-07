package ec.edu.ups.vista.Carrito;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CarritoAñadirView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JLabel lblCodigo, lblNombre, lblPrecio, lblCantidad, lblSubtotal, lblIva, lblTotal;
    private JTextField txtCodigo, txtNombre, txtPrecio, txtSubtotal, txtIva, txtTotal;
    private JComboBox<String> cbxCantidad;
    private JButton btnBuscar, btnAnadir, btnGuardar, btnLimpiar;
    private JTable tblProductos;

    public CarritoAñadirView(MensajeInternacionalizacionHandler mensajeHandler) {
        super("Carrito de Compras", true, true, false, true);
        this.mensajeHandler = mensajeHandler;

        // Monta el panel generado por el .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicialización de la tabla de productos
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("carrito.view.anadir.codigo"),
                mensajeHandler.get("carrito.view.anadir.nombre"),
                mensajeHandler.get("carrito.view.anadir.precio"),
                mensajeHandler.get("carrito.view.anadir.cantidad"),
                mensajeHandler.get("carrito.view.anadir.subtotal")
        });
        tblProductos.setModel(modelo);

        for (int i = 1; i <= 20; i++) {
            cbxCantidad.addItem(String.valueOf(i));
        }

        // Carga los textos según el idioma
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("carrito.view.anadir.titulo"));
        lblCodigo.setText(mensajeHandler.get("carrito.view.anadir.codigo") + ":");
        lblNombre.setText(mensajeHandler.get("carrito.view.anadir.nombre") + ":");
        lblPrecio.setText(mensajeHandler.get("carrito.view.anadir.precio") + ":");
        lblCantidad.setText(mensajeHandler.get("carrito.view.anadir.cantidad") + ":");
        lblSubtotal.setText(mensajeHandler.get("carrito.view.anadir.subtotal") + ":");
        lblIva.setText(mensajeHandler.get("carrito.view.anadir.iva") + ":");
        lblTotal.setText(mensajeHandler.get("carrito.view.anadir.total") + ":");

        btnBuscar.setText(mensajeHandler.get("carrito.view.anadir.buscar"));
        btnAnadir.setText(mensajeHandler.get("carrito.view.anadir.anadir"));
        btnGuardar.setText(mensajeHandler.get("carrito.view.anadir.guardar"));
        btnLimpiar.setText(mensajeHandler.get("carrito.view.anadir.limpiar"));
    }

    public MensajeInternacionalizacionHandler getMensajeInternacionalizacion() {
        return mensajeHandler;
    }

    // Getters para que el controlador pueda enlazar eventos y leer datos
    public JTextField getTxtCodigo()   { return txtCodigo; }
    public JTextField getTxtNombre()   { return txtNombre; }
    public JTextField getTxtPrecio()   { return txtPrecio; }
    public JComboBox<String> getCbxCantidad() { return cbxCantidad; }
    public JTable getTblProductos()    { return tblProductos; }
    public JTextField getTxtSubtotal() { return txtSubtotal; }
    public JTextField getTxtIva()      { return txtIva; }
    public JTextField getTxtTotal()    { return txtTotal; }
    public JButton getBtnBuscar()      { return btnBuscar; }
    public JButton getBtnAnadir()      { return btnAnadir; }
    public JButton getBtnGuardar()     { return btnGuardar; }
    public JButton getBtnLimpiar()     { return btnLimpiar; }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
