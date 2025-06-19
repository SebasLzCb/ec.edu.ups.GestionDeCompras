package ec.edu.ups.vista;

import javax.swing.*;

public class Principal extends JInternalFrame {
    private JMenuBar menuBar;
    private JMenu menuProducto;
    private JMenu menuCarrito;
    private JMenuItem menuItemCrearProducto;
    private JMenuItem menuItemEliminarProducto;
    private JMenuItem menuItemActualizarProducto;
    private JMenuItem menuItemBuscarProducto;
    private JMenuItem menuItemCrearCarrito;
    private JDesktopPane desktopPane;

    public Principal() {
        super("Sistema de Carrito de Compras En línea",
                true,   // resizable
                true,   // closable
                true,   // maximizable
                true);  // iconifiable

        menuBar = new JMenuBar();

        // Menú Producto
        menuProducto = new JMenu("Producto");
        menuItemCrearProducto     = new JMenuItem("Crear Producto");
        menuItemEliminarProducto  = new JMenuItem("Eliminar Producto");
        menuItemActualizarProducto= new JMenuItem("Actualizar Producto");
        menuItemBuscarProducto    = new JMenuItem("Buscar Producto");
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemActualizarProducto);
        menuProducto.add(menuItemBuscarProducto);
        menuBar.add(menuProducto);

        // Menú Crear Carrito
        menuCarrito = new JMenu("Crear Carrito");
        menuItemCrearCarrito = new JMenuItem("Añadir Carrito");
        menuCarrito.add(menuItemCrearCarrito);
        menuBar.add(menuCarrito);

        // Desktop Pane
        desktopPane = new JDesktopPane();

        getRootPane().setJMenuBar(menuBar);
        setContentPane(desktopPane);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
    }

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

    public JMenuItem getMenuItemCarrito() {
        return menuItemCrearCarrito;
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }
}

