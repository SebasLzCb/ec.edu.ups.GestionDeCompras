package ec.edu.ups.vista.Carrito;

import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarritoElimView extends JInternalFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JTextField txtCodigo;
    private JTable table1;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JLabel lblCodigo;

    private DefaultTableModel modeloTabla;

    public CarritoElimView(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;

        setTitle(mensajeHandler.get("carrito.view.eliminar.titulo"));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(500, 300);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout());
        txtCodigo = new JTextField(15);
        btnBuscar = new JButton();
        btnEliminar = new JButton();
        lblCodigo = new JLabel();

        panelSuperior.add(lblCodigo);
        panelSuperior.add(txtCodigo);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnEliminar);

        modeloTabla = new DefaultTableModel(new Object[]{"Código", "Usuario", "Total"}, 0);
        table1 = new JTable(modeloTabla);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(table1), BorderLayout.CENTER);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("carrito.view.eliminar.titulo"));
        lblCodigo.setText(mensajeHandler.get("carrito.view.eliminar.codigo") + ":");
        btnBuscar.setText(mensajeHandler.get("carrito.view.eliminar.buscar"));
        btnEliminar.setText(mensajeHandler.get("carrito.view.eliminar.eliminar"));

        modeloTabla.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("carrito.view.eliminar.codigo"),
                "Usuario",  // puedes internacionalizar si lo deseas
                "Total"     // idem
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
            mostrarMensaje(mensajeHandler.get("carrito.no.encontrado"));
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtCodigo.setText("");
        modeloTabla.setRowCount(0);
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }
}
