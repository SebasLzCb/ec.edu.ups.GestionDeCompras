package ec.edu.ups.vista.Carrito;

import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Locale;

public class CarritoDetallesView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTable tblDetalles;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private JLabel lblResumen;

    public CarritoDetallesView(MensajeInternacionalizacionHandler mensajeHandler) {
        super("Detalles de Carrito", true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        // Monta el panel generado por el .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializa la tabla de detalles sin datos
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("carrito.view.detalles.col_producto"),
                mensajeHandler.get("carrito.view.detalles.col_cantidad"),
                mensajeHandler.get("carrito.view.detalles.col_precio"),
                mensajeHandler.get("carrito.view.detalles.col_subtotal")
        });
        tblDetalles.setModel(modelo);

        // Carga los textos según el idioma actual
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("carrito.view.detalles.titulo"));
        lblResumen.setText(mensajeHandler.get("carrito.view.detalles.resumen") + ":");
        btnActualizar.setText(mensajeHandler.get("carrito.view.detalles.actualizar"));
        btnCancelar.setText(mensajeHandler.get("carrito.view.detalles.cancelar"));

        // Actualiza encabezados de tabla si cambia el idioma
        DefaultTableModel modelo = (DefaultTableModel) tblDetalles.getModel();
        modelo.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("carrito.view.detalles.col_producto"),
                mensajeHandler.get("carrito.view.detalles.col_cantidad"),
                mensajeHandler.get("carrito.view.detalles.col_precio"),
                mensajeHandler.get("carrito.view.detalles.col_subtotal")
        });
    }

    // Setea el resumen de totales
    public void setResumen(String texto) {
        lblResumen.setText(texto);
    }

    // Getters para el controlador
    public JTable getTblDetalles() { return tblDetalles; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnCancelar()  { return btnCancelar; }
    public JLabel getLblResumen()   { return lblResumen; }

}
