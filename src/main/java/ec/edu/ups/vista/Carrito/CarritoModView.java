package ec.edu.ups.vista.Carrito;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CarritoModView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;
    private JPanel panelPrincipal;
    private JLabel lblCodigo, lblTotal;
    private JTextField textCodigo, txtTotal;
    private JTable table1;
    private JButton btnBuscar, btnActualizar, btnCancelar;
    private DefaultTableModel modelo;

    public CarritoModView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("carrito.view.modificar.titulo"), true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        // Monta el panel generado por el .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializa modelo para la tabla de carritos
        modelo = new DefaultTableModel();
        table1.setModel(modelo);

        // Configura encabezados si ya tienes datos iniciales
        // modelo.setColumnIdentifiers(new Object[]{
        //     mensajeHandler.get("carrito.view.modificar.codigo"),
        //     mensajeHandler.get("carrito.view.modificar.total")
        // });

        // Carga textos y encabezados
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("carrito.view.modificar.titulo"));
        lblCodigo.setText(mensajeHandler.get("carrito.view.modificar.codigo") + ":");
        btnBuscar.setText(mensajeHandler.get("carrito.view.modificar.buscar"));
        btnActualizar.setText(mensajeHandler.get("carrito.view.modificar.actualizar"));
        btnCancelar.setText(mensajeHandler.get("carrito.view.modificar.cancelar"));
        lblTotal.setText(mensajeHandler.get("carrito.view.modificar.total") + ":");

        // Actualiza encabezados de tabla si aplica
        modelo.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("carrito.view.modificar.codigo"),
                mensajeHandler.get("carrito.view.modificar.total")
        });
    }

    // Getters para que el controlador pueda enlazar eventos y leer datos
    public JTextField getTxtCodigo()    { return textCodigo; }
    public JButton getBtnBuscar()       { return btnBuscar; }
    public JTable getTable1()           { return table1; }
    public JTextField getTxtTotal()     { return txtTotal; }
    public JButton getBtnActualizar()   { return btnActualizar; }
    public JButton getBtnCancelar()     { return btnCancelar; }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

}
