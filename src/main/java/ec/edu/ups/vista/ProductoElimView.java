package ec.edu.ups.vista;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.dao.ProductoDAO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductoElimView extends JInternalFrame {
    private final ProductoDAO productoDAO;
    private JPanel panelPrincipal;
    private JComboBox<String> comboFiltro;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTable tablaResultado;
    private DefaultTableModel modelResultado;
    private JButton btnEliminar;

    public ProductoElimView(ProductoDAO dao) {
        super("Eliminar Producto", true, true, true, true);
        this.productoDAO = dao;
        initializeComponents();
        configureActions();
    }

    private void initializeComponents() {
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Eliminar Producto",
                TitledBorder.LEADING, TitledBorder.TOP
        ));
        setContentPane(panelPrincipal);

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pnlBusqueda.add(new JLabel("Buscar por:"));
        comboFiltro = new JComboBox<>(new String[]{"Código", "Nombre"});
        pnlBusqueda.add(comboFiltro);
        txtBusqueda = new JTextField(15);
        pnlBusqueda.add(txtBusqueda);
        btnBuscar = new JButton("Buscar");
        pnlBusqueda.add(btnBuscar);
        panelPrincipal.add(pnlBusqueda, BorderLayout.NORTH);

        modelResultado = new DefaultTableModel(new Object[]{"Código", "Nombre", "Precio"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaResultado = new JTable(modelResultado);
        panelPrincipal.add(new JScrollPane(tablaResultado), BorderLayout.CENTER);

        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnEliminar = new JButton("Eliminar");
        pnlAcciones.add(btnEliminar);
        panelPrincipal.add(pnlAcciones, BorderLayout.SOUTH);

        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void configureActions() {
        btnBuscar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                modelResultado.setRowCount(0);
                String criterio = comboFiltro.getSelectedItem().toString();
                String valor = txtBusqueda.getText().trim().toLowerCase();
                if (criterio.equals("Código")) {
                    try {
                        int cod = Integer.parseInt(valor);
                        Producto p = productoDAO.buscarPorCodigo(cod);
                        if (p != null) {
                            modelResultado.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(ProductoElimView.this, "Código inválido");
                    }
                } else {
                    for (Producto p : productoDAO.listarTodos()) {
                        if (p.getNombre().toLowerCase().startsWith(valor)) {
                            modelResultado.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
                        }
                    }
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                int row = tablaResultado.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(ProductoElimView.this, "Selecciona un producto");
                    return;
                }
                int cod = (int) modelResultado.getValueAt(row, 0);
                productoDAO.eliminar(cod);
                modelResultado.removeRow(row);
                JOptionPane.showMessageDialog(ProductoElimView.this, "Producto eliminado");
            }
        });
    }

    public JTable getTablaResultado() { return tablaResultado; }
    public JComboBox<String> getComboFiltro() { return comboFiltro; }
    public JTextField getTxtBusqueda() { return txtBusqueda; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public DefaultTableModel getModelResultado() { return modelResultado; }
}
