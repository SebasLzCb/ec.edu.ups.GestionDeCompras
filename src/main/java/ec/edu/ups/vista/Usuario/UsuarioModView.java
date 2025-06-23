package ec.edu.ups.vista.Usuario;

import ec.edu.ups.modelo.Rol;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuarioModView extends JInternalFrame {
    private JPanel panel;
    private JPasswordField txtPassword;
    private JComboBox<Rol>   cbxRol;
    private JTable           tabla;
    private DefaultTableModel model;
    private JButton           btnActualizar;
    private JButton           btnRefrescar;

    public UsuarioModView() {
        super("Modificar Usuario", true, true, true, true);

        panel = new JPanel(new BorderLayout(5,5));

        // --- Zona de inputs en norte ---
        JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        pnlNorth.add(new JLabel("Nueva contraseña:"));
        txtPassword = new JPasswordField(10);
        pnlNorth.add(txtPassword);
        pnlNorth.add(new JLabel("Rol:"));
        cbxRol = new JComboBox<>(Rol.values());
        pnlNorth.add(cbxRol);
        btnRefrescar = new JButton("Refrescar");
        pnlNorth.add(btnRefrescar);
        panel.add(pnlNorth, BorderLayout.NORTH);

        // --- Tabla ---
        model = new DefaultTableModel(new Object[]{"Usuario","Rol"},0){
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        tabla = new JTable(model);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- Botón actualizar ---
        btnActualizar = new JButton("Guardar Cambios");
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSouth.add(btnActualizar);
        panel.add(pnlSouth, BorderLayout.SOUTH);

        setContentPane(panel);
        setSize(500,400);
    }

    // getters que tu controlador necesita
    public JPasswordField     getTxtPassword()  { return txtPassword; }
    public JComboBox<Rol>     getCbxRol()       { return cbxRol; }
    public JTable             getTableUsuarios(){ return tabla; }
    public DefaultTableModel  getTableModel()   { return model; }
    public JButton            getBtnActualizar(){ return btnActualizar; }
    public JButton            getBtnRefrescar() { return btnRefrescar; }
}
