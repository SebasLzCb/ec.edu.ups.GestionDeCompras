package ec.edu.ups.vista.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuarioListaView extends JInternalFrame {
    private JPanel panel;
    private JTable tabla;
    private DefaultTableModel model;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnRefrescar;
    private JComboBox<String> cbxFiltro;

    public UsuarioListaView() {
        super("Lista de Usuarios", true, true, true, true);
        panel = new JPanel(new BorderLayout(5,5));

        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlNorte.add(new JLabel("Buscar por:"));
        cbxFiltro = new JComboBox<>(new String[]{"Usuario","Rol"});
        pnlNorte.add(cbxFiltro);
        txtBuscar = new JTextField(10);
        pnlNorte.add(txtBuscar);
        btnBuscar = new JButton("Buscar");
        pnlNorte.add(btnBuscar);
        btnRefrescar = new JButton("Refrescar");
        pnlNorte.add(btnRefrescar);
        panel.add(pnlNorte, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Usuario","Rol"},0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        tabla = new JTable(model);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        setContentPane(panel);
        setSize(500,400);
    }

    public JComboBox<String> getComboFiltro() { return cbxFiltro; }
    public JTextField     getTxtBuscar()    { return txtBuscar; }
    public JButton        getBtnBuscar()    { return btnBuscar; }
    public JButton        getBtnRefrescar() { return btnRefrescar; }
    public JTable         getTableUsuarios(){ return tabla; }
    public DefaultTableModel getTableModel(){ return model; }
}
