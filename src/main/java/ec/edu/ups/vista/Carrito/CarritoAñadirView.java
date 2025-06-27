package ec.edu.ups.vista.Carrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CarritoAñadirView extends JInternalFrame {

    private JButton btnBuscar;
    private JButton btnAnadir;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTable tblProductos;
    private JTextField txtSubtotal;
    private JTextField txtIva;
    private JTextField txtTotal;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JComboBox<String> cbxCantidad;
    private JPanel panelPrincipal;

    private DefaultTableModel modelo;

    public CarritoAñadirView() {
        super("Carrito de Compras", true, true, false, true);
        setSize(700, 550);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        initComponents();
        setContentPane(panelPrincipal);

        cargarDatos();
    }

    private void initComponents() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));

        txtCodigo = new JTextField(10);
        txtNombre = new JTextField(20); txtNombre.setEditable(false);
        txtPrecio = new JTextField(10); txtPrecio.setEditable(false);
        cbxCantidad = new JComboBox<>();

        btnBuscar = new JButton("Buscar");
        btnAnadir = new JButton("Añadir");
        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");

        JPanel linea1 = new JPanel();
        linea1.add(new JLabel("Código:"));
        linea1.add(txtCodigo);
        linea1.add(btnBuscar);

        JPanel linea2 = new JPanel();
        linea2.add(new JLabel("Nombre:"));
        linea2.add(txtNombre);

        JPanel linea3 = new JPanel();
        linea3.add(new JLabel("Precio:"));
        linea3.add(txtPrecio);

        JPanel linea4 = new JPanel();
        linea4.add(new JLabel("Cantidad:"));
        linea4.add(cbxCantidad);
        linea4.add(btnAnadir);

        panelDatos.add(linea1);
        panelDatos.add(linea2);
        panelDatos.add(linea3);
        panelDatos.add(linea4);

        panelPrincipal.add(panelDatos);

        modelo = new DefaultTableModel(new Object[]{"Código", "Nombre", "Precio", "Cantidad", "Subtotal"}, 0);
        tblProductos = new JTable(modelo);
        panelPrincipal.add(new JScrollPane(tblProductos));

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelPrincipal.add(panelBotones);

        JPanel panelTotales = new JPanel();
        txtSubtotal = new JTextField(8); txtSubtotal.setEditable(false);
        txtIva = new JTextField(8);      txtIva.setEditable(false);
        txtTotal = new JTextField(8);    txtTotal.setEditable(false);

        panelTotales.add(new JLabel("Subtotal:"));
        panelTotales.add(txtSubtotal);
        panelTotales.add(new JLabel("IVA (12%):"));
        panelTotales.add(txtIva);
        panelTotales.add(new JLabel("Total:"));
        panelTotales.add(txtTotal);

        panelPrincipal.add(panelTotales);
    }

    private void cargarDatos() {
        cbxCantidad.removeAllItems();
        for (int i = 1; i <= 20; i++) {
            cbxCantidad.addItem(String.valueOf(i));
        }
    }

    // Métodos getter
    public JButton getBtnAnadir() { return btnAnadir; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JTextField getTxtCodigo() { return txtCodigo; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JTable getTblProductos() { return tblProductos; }
    public JTextField getTxtSubtotal() { return txtSubtotal; }
    public JTextField getTxtIva() { return txtIva; }
    public JTextField getTxtTotal() { return txtTotal; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JComboBox<String> getCbxCantidad() { return cbxCantidad; }
    public JPanel getPanelPrincipal() { return panelPrincipal; }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
