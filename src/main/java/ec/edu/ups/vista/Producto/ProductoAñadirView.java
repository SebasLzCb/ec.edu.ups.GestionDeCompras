package ec.edu.ups.vista.Producto;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;

public class ProductoAñadirView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnAceptar;
    private JButton btnLimpiar;
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JPanel JPaneIngresarPrd;


    private final MensajeInternacionalizacionHandler mensajeHandler;

    public ProductoAñadirView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("producto.view.anadir.titulo"), true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("producto.view.anadir.titulo"));
        lblCodigo.setText(mensajeHandler.get("producto.view.anadir.codigo") + ":");
        lblNombre.setText(mensajeHandler.get("producto.view.anadir.nombre") + ":");
        lblPrecio.setText(mensajeHandler.get("producto.view.anadir.precio") + ":");
        btnAceptar.setText(mensajeHandler.get("producto.view.anadir.aceptar"));
        btnLimpiar.setText(mensajeHandler.get("producto.view.anadir.limpiar"));
    }

    public JTextField getTxtCodigo()   { return txtCodigo; }
    public JTextField getTxtNombre()   { return txtNombre; }
    public JTextField getTxtPrecio()   { return txtPrecio; }
    public JButton    getBtnAceptar()  { return btnAceptar; }
    public JButton    getBtnLimpiar()  { return btnLimpiar; }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
    }

    private void createUIComponents() {
        JPaneIngresarPrd = new JPanel();
    }

}
