package ec.edu.ups.vista.Usuario;

import javax.swing.*;
import java.awt.*;

public class UsuarioRegistroView extends JInternalFrame {
    private JPanel panel;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> cbxRol;
    private JButton btnCrear;     // el único botón
    private JButton btnLimpiar;

    public UsuarioRegistroView() {
        super("Registrar Usuario", true, true, true, true);
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0;
        panel.add(new JLabel("Usuario:"), gbc);
        txtUsuario = new JTextField(15);
        gbc.gridx=1;
        panel.add(txtUsuario, gbc);

        gbc.gridx=0; gbc.gridy=1;
        panel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx=1;
        panel.add(txtPassword, gbc);

        gbc.gridx=0; gbc.gridy=2;
        panel.add(new JLabel("Rol:"), gbc);
        cbxRol = new JComboBox<>(new String[]{"Administrador","Cliente"});
        gbc.gridx=1;
        panel.add(cbxRol, gbc);

        // inicializamos btnCrear y btnLimpiar
        btnCrear = new JButton("Crear");
        btnLimpiar = new JButton("Limpiar");
        JPanel pnl = new JPanel();
        pnl.add(btnCrear);
        pnl.add(btnLimpiar);
        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2;
        panel.add(pnl, gbc);

        setContentPane(panel);
        setSize(350,250);
    }

    // getters
    public JTextField getTxtUsuario()         { return txtUsuario; }
    public JPasswordField getTxtPassword()    { return txtPassword; }
    public JComboBox<String> getCbxRol()      { return cbxRol; }
    public JButton getBtnCrear()              { return btnCrear; }
    public JButton getBtnLimpiar()            { return btnLimpiar; }
}
