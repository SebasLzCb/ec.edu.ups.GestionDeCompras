package ec.edu.ups.vista.Carrito;

import javax.swing.*;
import java.awt.*;

public class CarritoFormView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField      txtCodigo;
    private JComboBox<String> cbxUsuario;
    private JButton         btnAbrirItems;
    private JButton         btnGuardar;
    private JButton         btnCancelar;

    public CarritoFormView() {
        super("Crear / Editar Carrito", true, true, true, true);
        initComponents();
        setContentPane(panelPrincipal);
        setSize(500,300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx=0; gbc.gridy=0;
        panelPrincipal.add(new JLabel("Código:"), gbc);
        txtCodigo = new JTextField(6);
        gbc.gridx=1;
        panelPrincipal.add(txtCodigo, gbc);

        gbc.gridx=0; gbc.gridy=1;
        panelPrincipal.add(new JLabel("Usuario:"), gbc);
        cbxUsuario = new JComboBox<>();
        gbc.gridx=1;
        panelPrincipal.add(cbxUsuario, gbc);

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2;
        btnAbrirItems = new JButton("Gestionar Ítems del Carrito");
        panelPrincipal.add(btnAbrirItems, gbc);

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,0));
        btnGuardar  = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        pnlBtns.add(btnGuardar);
        pnlBtns.add(btnCancelar);
        gbc.gridy=3;
        panelPrincipal.add(pnlBtns, gbc);
    }

    public JTextField getTxtCodigo()            { return txtCodigo; }
    public JComboBox<String> getCbxUsuario()    { return cbxUsuario; }
    public JButton getBtnAbrirItems()           { return btnAbrirItems; }
    public JButton getBtnGuardar()              { return btnGuardar; }
    public JButton getBtnCancelar()             { return btnCancelar; }
}
