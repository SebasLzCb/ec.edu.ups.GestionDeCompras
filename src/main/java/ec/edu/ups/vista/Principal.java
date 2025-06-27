package ec.edu.ups.vista;

import javax.swing.*;

public class Principal extends JFrame {

    private JMenuBar menuBar;

    private JMenu menuProducto;
    private JMenu menuCarrito;
    private JMenu menuUsuario;

    private JMenuItem menuItemCrearProducto;
    private JMenuItem menuItemEliminarProducto;
    private JMenuItem menuItemActualizarProducto;
    private JMenuItem menuItemBuscarProducto;

    private JMenuItem menuItemCrearCarrito;
    private JMenuItem menuItemBuscarCarrito;
    private JMenuItem menuItemModificarCarrito;

    private JMenuItem menuItemRegistrarUsuario;
    private JMenuItem menuItemListarUsuarios;
    private JMenuItem menuItemModificarUsuario;
    private JMenuItem menuItemEliminarUsuario;
    private JMenuItem menuItemCerrarSesion;

    private MiJDesktopPane desktopPane;

    public Principal() {
        desktopPane = new MiJDesktopPane();
        menuBar = new JMenuBar();

        // Menú Producto
        menuProducto = new JMenu("Producto");
        menuItemCrearProducto      = new JMenuItem("Crear Producto");
        menuItemEliminarProducto   = new JMenuItem("Eliminar Producto");
        menuItemActualizarProducto = new JMenuItem("Actualizar Producto");
        menuItemBuscarProducto     = new JMenuItem("Buscar Producto");
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemActualizarProducto);
        menuProducto.add(menuItemBuscarProducto);

        // Menú Carrito
        menuCarrito = new JMenu("Carrito");
        menuItemCrearCarrito       = new JMenuItem("Crear Carrito");
        menuItemBuscarCarrito      = new JMenuItem("Buscar Carrito");
        menuItemModificarCarrito   = new JMenuItem("Modificar Carrito");
        menuCarrito.add(menuItemCrearCarrito);
        menuCarrito.add(menuItemBuscarCarrito);
        menuCarrito.add(menuItemModificarCarrito);

        // Menú Usuario
        menuUsuario = new JMenu("Usuarios");
        menuItemRegistrarUsuario   = new JMenuItem("Registrar Usuario");
        menuItemListarUsuarios     = new JMenuItem("Listar Usuarios");
        menuItemModificarUsuario   = new JMenuItem("Modificar Usuario");
        menuItemEliminarUsuario    = new JMenuItem("Eliminar Usuario");
        menuItemCerrarSesion       = new JMenuItem("Cerrar Sesión");
        menuUsuario.add(menuItemRegistrarUsuario);
        menuUsuario.add(menuItemListarUsuarios);
        menuUsuario.add(menuItemModificarUsuario);
        menuUsuario.add(menuItemEliminarUsuario);
        menuUsuario.addSeparator();
        menuUsuario.add(menuItemCerrarSesion);

        // Agregar al menú principal
        menuBar.add(menuProducto);
        menuBar.add(menuCarrito);
        menuBar.add(menuUsuario);

        setJMenuBar(menuBar);
        setContentPane(desktopPane);
        setTitle("Sistema de Carrito de Compras En Línea");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ❌ Ya no se muestra automáticamente
        // setVisible(true);
    }

    // Getters para menú Producto
    public JMenuItem getMenuItemCrearProducto() {
        return menuItemCrearProducto;
    }

    public JMenuItem getMenuItemEliminarProducto() {
        return menuItemEliminarProducto;
    }

    public JMenuItem getMenuItemActualizarProducto() {
        return menuItemActualizarProducto;
    }

    public JMenuItem getMenuItemBuscarProducto() {
        return menuItemBuscarProducto;
    }

    // Getters para menú Carrito
    public JMenuItem getMenuItemCrearCarrito() {
        return menuItemCrearCarrito;
    }

    public JMenuItem getMenuItemBuscarCarrito() {
        return menuItemBuscarCarrito;
    }

    public JMenuItem getMenuItemModificarCarrito() {
        return menuItemModificarCarrito;
    }

    // Getters para menú Usuario
    public JMenuItem getMenuItemRegistrarUsuario() {
        return menuItemRegistrarUsuario;
    }

    public JMenuItem getMenuItemListarUsuarios() {
        return menuItemListarUsuarios;
    }

    public JMenuItem getMenuItemModificarUsuario() {
        return menuItemModificarUsuario;
    }

    public JMenuItem getMenuItemEliminarUsuario() {
        return menuItemEliminarUsuario;
    }

    public JMenuItem getMenuItemCerrarSesion() {
        return menuItemCerrarSesion;
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void deshabilitarMenusAdministrador() {
        getMenuItemCrearProducto().setEnabled(false);
        getMenuItemBuscarProducto().setEnabled(false);
        getMenuItemActualizarProducto().setEnabled(false);
        getMenuItemEliminarProducto().setEnabled(false);
        getMenuItemRegistrarUsuario().setEnabled(false);
        getMenuItemListarUsuarios().setEnabled(false);
        getMenuItemModificarUsuario().setEnabled(false);
        getMenuItemEliminarUsuario().setEnabled(false);
        getMenuItemCrearCarrito().setEnabled(false);
        getMenuItemModificarCarrito().setEnabled(false);
    }
}
