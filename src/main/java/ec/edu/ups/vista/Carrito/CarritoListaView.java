package ec.edu.ups.vista.Carrito;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CarritoListaView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;
    private JPanel panelPrincipal;
    private JLabel lblCodigo, lblTotal;
    private JTextField txtCodigo, txtTotal;
    private JButton btnBuscar, btnListar, btnDetalles;
    private JTable tablaCarrito;
    private DefaultTableModel modelo;

    public CarritoListaView(MensajeInternacionalizacionHandler mensajeHandler) {
        super("Lista de Carritos", true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        // Monta el panel generado por el .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializa modelo y tabla
        modelo = new DefaultTableModel();
        tablaCarrito.setModel(modelo);

        // Inicializa campos y botones si hace falta

        // Carga encabezados y textos
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("carrito.view.listar.titulo"));
        lblCodigo.setText(mensajeHandler.get("carrito.view.listar.codigo") + ":");
        btnBuscar.setText(mensajeHandler.get("carrito.view.listar.buscar"));
        btnListar.setText(mensajeHandler.get("carrito.view.listar.listar"));
        btnDetalles.setText(mensajeHandler.get("carrito.view.listar.detalles"));
        lblTotal.setText(mensajeHandler.get("carrito.view.listar.total") + ":");

        // Actualiza columnas de la tabla
        modelo.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("carrito.view.listar.codigo"),
                mensajeHandler.get("carrito.view.listar.usuario"),
                mensajeHandler.get("carrito.view.listar.total")
        });
    }

    public String getTxtCodigo() {
        return txtCodigo.getText().trim();
    }

    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnListar() { return btnListar; }
    public JButton getBtnDetalles() { return btnDetalles; }

    public JTable getTablaCarrito() { return tablaCarrito; }
    public JTextField getTxtTotal() { return txtTotal; }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public MensajeInternacionalizacionHandler getMensajeInternacionalizacion() {
        return mensajeHandler;
    }

}
