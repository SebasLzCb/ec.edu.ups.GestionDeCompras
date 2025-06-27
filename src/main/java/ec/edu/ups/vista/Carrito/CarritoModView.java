package ec.edu.ups.vista.Carrito;

import javax.swing.*;

public class CarritoModView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField textCodigo;
    private JButton btnBuscar;
    private JTable table1;
    private JButton btnCancelar;
    private JTextField txtTotal;
    private JScrollBar scrollBar1;
    private JButton btnActualizar;

    public CarritoModView() {
        setContentPane(panelPrincipal);
        setTitle("Modificar Carrito");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocation(30, 30);
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
