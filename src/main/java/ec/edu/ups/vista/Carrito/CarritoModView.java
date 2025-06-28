package ec.edu.ups.vista.Carrito;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;

public class CarritoModView extends JInternalFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTextField textCodigo;
    private JButton btnBuscar;
    private JTable table1;
    private JButton btnCancelar;
    private JTextField txtTotal;
    private JButton btnActualizar;

    public CarritoModView(MensajeInternacionalizacionHandler mensajeHandler) {
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
        setTitle(mensajeHandler.get("carrito.view.modificar.titulo"));
        btnBuscar.setText(mensajeHandler.get("carrito.view.modificar.buscar"));
        btnActualizar.setText(mensajeHandler.get("carrito.view.modificar.actualizar"));
        btnCancelar.setText(mensajeHandler.get("carrito.view.modificar.cancelar"));
        // Agrega más cambios si tienes etiquetas adicionales
    }

    public JTextField getTxtCodigo() {
        return textCodigo;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JTable getTable1() {
        return table1;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JTextField getTxtTotal() {
        return txtTotal;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
