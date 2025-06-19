package ec.edu.ups.vista;

import ec.edu.ups.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoListaView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JComboBox<String> comboFiltro;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JButton btnRefrescar;
    private JTable tablaProductos;
    private DefaultTableModel model;

    public ProductoListaView() {
        super("Listado de Productos", true, true, true, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);

        panelPrincipal = new JPanel(new BorderLayout(5,5));

        // Barra de búsqueda + refrescar
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pnlBusqueda.add(new JLabel("Buscar por:"));
        comboFiltro = new JComboBox<>(new String[]{"Código", "Nombre"});
        pnlBusqueda.add(comboFiltro);
        txtBusqueda = new JTextField(15);
        pnlBusqueda.add(txtBusqueda);
        btnBuscar = new JButton("Buscar");
        pnlBusqueda.add(btnBuscar);
        btnRefrescar = new JButton("Refrescar");
        pnlBusqueda.add(btnRefrescar);
        panelPrincipal.add(pnlBusqueda, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Código","Nombre","Precio"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaProductos = new JTable(model);
        panelPrincipal.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    public void listarProductos(List<Producto> productos) {
        model.setRowCount(0);
        for (Producto p : productos) {
            model.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    public JComboBox<String> getComboFiltro() { return comboFiltro; }
    public JTextField getTxtBusqueda()       { return txtBusqueda; }
    public JButton getBtnBuscar()            { return btnBuscar; }
    public JButton getBtnRefrescar()         { return btnRefrescar; }
    public JTable getTablaProductos()        { return tablaProductos; }
}
