package ec.edu.ups.vista.Carrito;

import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CarritoElimView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;
    private JPanel panelPrincipal;
    private JLabel lblCodigo;
    private JTextField txtCodigo;
    private JButton btnBuscar, btnEliminar;
    private JTable table1;
    private DefaultTableModel modeloTabla;

    public CarritoElimView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("carrito.view.eliminar.titulo"), true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        // Monta el panel del .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializa el modelo de la tabla y la enlaza
        modeloTabla = new DefaultTableModel();
        table1.setModel(modeloTabla);

        // Inicializa combo, labels, etc. si aplica (aquí solo campo de texto)

        // Carga los textos y encabezados
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("carrito.view.eliminar.titulo"));
        lblCodigo.setText(mensajeHandler.get("carrito.view.eliminar.codigo") + ":");
        btnBuscar.setText(mensajeHandler.get("carrito.view.eliminar.buscar"));
        btnEliminar.setText(mensajeHandler.get("carrito.view.eliminar.eliminar"));
        modeloTabla.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("carrito.view.eliminar.codigo"),
                mensajeHandler.get("carrito.view.eliminar.usuario"),
                mensajeHandler.get("carrito.view.eliminar.total")
        });
    }

    public String getCodigoIngresado() {
        return txtCodigo.getText().trim();
    }

    public void mostrarResultado(Carrito carrito) {
        modeloTabla.setRowCount(0);
        if (carrito != null) {
            modeloTabla.addRow(new Object[]{
                    carrito.getCodigo(),
                    carrito.getUsuario().getUsername(),
                    carrito.calcularTotal()
            });
        } else {
            JOptionPane.showMessageDialog(this, mensajeHandler.get("carrito.no.encontrado"));
        }
    }

    public void limpiarCampos() {
        txtCodigo.setText("");
        modeloTabla.setRowCount(0);
    }

    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnEliminar() { return btnEliminar; }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}