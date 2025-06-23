package ec.edu.ups.vista.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuarioElimView extends JInternalFrame {
    private JPanel panel;
    private JComboBox<String> cbxFiltro;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tabla;
    private DefaultTableModel model;
    private JButton btnEliminar;

    public UsuarioElimView() {
        super("Eliminar Usuario", true, true, true, true);
        panel = new JPanel(new BorderLayout(5,5));
        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlNorte.add(new JLabel("Buscar por:"));
        cbxFiltro = new JComboBox<>(new String[]{"Usuario","Rol"});
        pnlNorte.add(cbxFiltro);
        txtBuscar = new JTextField(10);
        pnlNorte.add(txtBuscar);
        btnBuscar = new JButton("Buscar");
        pnlNorte.add(btnBuscar);
        panel.add(pnlNorte, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Usuario","Rol"},0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        tabla = new JTable(model);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnEliminar = new JButton("Eliminar");
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSur.add(btnEliminar);
        panel.add(pnlSur, BorderLayout.SOUTH);

        setContentPane(panel);
        setSize(450,350);
    }

    public JTable         getTableUsuarios(){ return tabla; }
    public DefaultTableModel getTableModel(){ return model; }
    public JButton        getBtnEliminar()  { return btnEliminar; }
    public JComboBox<String> getCbxFiltro() { return cbxFiltro; }
    public JTextField     getTxtBuscar()    { return txtBuscar; }
    public JButton        getBtnBuscar()    { return btnBuscar; }
}
