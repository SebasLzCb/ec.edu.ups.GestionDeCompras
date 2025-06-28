package ec.edu.ups.vista.Carrito;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;

public class CarritoListaView extends JInternalFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTextField txtCodigo;
    private JButton btnBuscar;
    private JButton btnListar;
    private JTable table1;
    private JTextField txtTotal;
    private JButton btnDetalles;
    private JTextArea txtDetalles;

    public CarritoListaView(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocation(30, 30);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("carrito.view.listar.titulo"));
        btnBuscar.setText(mensajeHandler.get("carrito.view.listar.buscar"));
        btnListar.setText(mensajeHandler.get("carrito.view.listar.listar"));
        btnDetalles.setText(mensajeHandler.get("carrito.view.listar.detalles"));
        // Puedes añadir etiquetas en el panel si deseas también internacionalizarlas.
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public JTable getTablaCarrito() {
        return table1;
    }

    public JTextField getTxtTotal() {
        return txtTotal;
    }

    public JButton getBtnDetalles() {
        return btnDetalles;
    }

    public JTextArea getTxtDetalles() {
        return txtDetalles;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
