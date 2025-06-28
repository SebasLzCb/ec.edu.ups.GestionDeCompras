package ec.edu.ups.vista.Producto;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.util.List;

public class ProductoAñadirView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnAceptar;
    private JButton btnLimpiar;
    private JPanel JPaneIngresarPrd;
    private JLabel lblCodigo;
    private JLabel lblPrecio;
    private JLabel lblNombre;

    private MensajeInternacionalizacionHandler mensajeHandler;

    public ProductoAñadirView(MensajeInternacionalizacionHandler mensajeHandler) {
        super("", true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        actualizarIdioma();
        pack();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("producto.view.anadir.titulo"));
        btnAceptar.setText(mensajeHandler.get("producto.view.anadir.aceptar"));
        btnLimpiar.setText(mensajeHandler.get("producto.view.anadir.limpiar"));

        lblCodigo.setText(mensajeHandler.get("producto.view.anadir.codigo"));
        lblNombre.setText(mensajeHandler.get("producto.view.anadir.nombre"));
        lblPrecio.setText(mensajeHandler.get("producto.view.anadir.precio"));
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public JButton getBtnAceptar() {
        return btnAceptar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
    }

    public void mostrarProductos(List<Producto> productos) {
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }
}
