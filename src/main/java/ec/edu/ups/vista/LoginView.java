package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final JTextField txtUsuario;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;
    private final JButton btnRegistrar;

    public LoginView() {
        super("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        p.add(new JLabel("Usuario:"), gbc);
        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        p.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        p.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        p.add(txtPassword, gbc);

        btnLogin     = new JButton("Entrar");
        btnRegistrar = new JButton("Registrar");
        JPanel botones = new JPanel();
        botones.add(btnLogin);
        botones.add(btnRegistrar);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        p.add(botones, gbc);

        setContentPane(p);
        pack();
        setLocationRelativeTo(null);
    }

    public String getUsuario()     { return txtUsuario.getText().trim(); }
    public String getPassword()    { return new String(txtPassword.getPassword()); }
    public JButton getBtnLogin()   { return btnLogin; }
    public JButton getBtnRegistrar(){ return btnRegistrar; }
    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
