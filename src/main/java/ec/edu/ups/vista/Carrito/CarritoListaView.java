package ec.edu.ups.vista.Carrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarritoListaView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTable  tableCarritos;
    private DefaultTableModel tableModel;
    private JButton btnRefrescar;
    private JButton btnBuscar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JComboBox<String> comboFiltro;
    private JTextField txtBuscar;

    public CarritoListaView() {
        super("Listado de Carritos", true, true, true, true);
        initComponents();
        setContentPane(panelPrincipal);
        setSize(600,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        panelPrincipal = new JPanel(new BorderLayout(5,5));

        JPanel pnlFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,5));
        comboFiltro = new JComboBox<>(new String[]{"Código", "Usuario"});
        txtBuscar    = new JTextField(10);
        btnBuscar    = new JButton("Buscar");
        btnRefrescar = new JButton("Refrescar");
        pnlFiltro.add(new JLabel("Filtrar por:"));
        pnlFiltro.add(comboFiltro);
        pnlFiltro.add(txtBuscar);
        pnlFiltro.add(btnBuscar);
        pnlFiltro.add(btnRefrescar);
        panelPrincipal.add(pnlFiltro, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Código","Usuario","Total"},
                0
        ) {
            @Override public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tableCarritos = new JTable(tableModel);
        panelPrincipal.add(new JScrollPane(tableCarritos), BorderLayout.CENTER);

        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,5));
        btnEditar   = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        pnlAcciones.add(btnEditar);
        pnlAcciones.add(btnEliminar);
        panelPrincipal.add(pnlAcciones, BorderLayout.SOUTH);
    }

    public JTable getTableCarritos()         { return tableCarritos; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JButton getBtnRefrescar()         { return btnRefrescar; }
    public JButton getBtnBuscar()            { return btnBuscar; }
    public JButton getBtnEditar()            { return btnEditar; }
    public JButton getBtnEliminar()          { return btnEliminar; }
    public JComboBox<String> getComboFiltro(){ return comboFiltro; }
    public JTextField getTxtBuscar()         { return txtBuscar; }
}
