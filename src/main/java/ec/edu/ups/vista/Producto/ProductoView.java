package ec.edu.ups.vista.Producto;

import javax.swing.*;

public class ProductoView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnAceptar;
    private JButton btnLimpiar;

    public ProductoView() {
        super("Datos del Producto", true, true, true, true);
        setContentPane(panelPrincipal);
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public JTextField getTxtCodigo() { return txtCodigo; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JButton getBtnAceptar()   { return btnAceptar; }
    public JButton getBtnLimpiar()   { return btnLimpiar; }
    public void limpiarCampos()      { txtCodigo.setText(""); txtNombre.setText(""); txtPrecio.setText(""); }
    public void mostrarMensaje(String m) { JOptionPane.showMessageDialog(this, m); }
}
