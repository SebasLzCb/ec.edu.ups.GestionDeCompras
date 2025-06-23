package ec.edu.ups.vista.Producto;

import ec.edu.ups.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoModView extends JInternalFrame {
    private JTable tableProductos;
    private DefaultTableModel tableModel;
    private JButton btnActualizar;
    private JButton btnRefrescar;

    public ProductoModView() {
        super("Modificar Productos", true, true, true, true);

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        btnRefrescar = new JButton("Refrescar");
        toolbar.add(btnRefrescar);
        getContentPane().add(toolbar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Código", "Nombre", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        tableProductos = new JTable(tableModel);
        tableProductos.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(tableProductos);
        getContentPane().add(scroll, BorderLayout.CENTER);

        btnActualizar = new JButton("Guardar Cambios");
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlAcciones.add(btnActualizar);
        getContentPane().add(pnlAcciones, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void listarProductos(List<Producto> productos) {
        tableModel.setRowCount(0);
        for (Producto p : productos) {
            tableModel.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getPrecio()
            });
        }
    }

    public JTable getTableProductos() {
        return tableProductos;
    }

    public void setTableProductos(JTable tableProductos) {
        this.tableProductos = tableProductos;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public void setBtnActualizar(JButton btnActualizar) {
        this.btnActualizar = btnActualizar;
    }

    public JButton getBtnRefrescar() {
        return btnRefrescar;
    }

    public void setBtnRefrescar(JButton btnRefrescar) {
        this.btnRefrescar = btnRefrescar;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }
}
