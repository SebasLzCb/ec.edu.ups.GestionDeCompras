package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;

public class Principal extends JFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JMenuBar menuBar;

    private JMenu menuProducto;
    private JMenu menuCarrito;
    private JMenu menuUsuario;

    private JMenu menuIdioma;
    private JMenuItem menuItemIdiomaEspanol;
    private JMenuItem menuItemIdiomaIngles;
    private JMenuItem menuItemIdiomaFrances;

    private JMenuItem menuItemCrearProducto;
    private JMenuItem menuItemEliminarProducto;
    private JMenuItem menuItemActualizarProducto;
    private JMenuItem menuItemBuscarProducto;

    private JMenuItem menuItemCrearCarrito;
    private JMenuItem menuItemBuscarCarrito;
    private JMenuItem menuItemModificarCarrito;
    private JMenuItem menuItemEliminarCarrito;

    private JMenuItem menuItemRegistrarUsuario;
    private JMenuItem menuItemListarUsuarios;
    private JMenuItem menuItemModificarUsuario;
    private JMenuItem menuItemEliminarUsuario;
    private JMenuItem menuItemCerrarSesion;

    private MiJDesktopPane desktopPane;

    public Principal(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;

        desktopPane = new MiJDesktopPane();
        menuBar = new JMenuBar();

        menuProducto = new JMenu("Producto");
        menuItemCrearProducto = new JMenuItem("Crear Producto");
        menuItemEliminarProducto = new JMenuItem("Eliminar Producto");
        menuItemActualizarProducto = new JMenuItem("Actualizar Producto");
        menuItemBuscarProducto = new JMenuItem("Buscar Producto");
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemActualizarProducto);
        menuProducto.add(menuItemBuscarProducto);

        menuCarrito = new JMenu("Carrito");
        menuItemCrearCarrito = new JMenuItem("Crear Carrito");
        menuItemBuscarCarrito = new JMenuItem("Buscar Carrito");
        menuItemModificarCarrito = new JMenuItem("Modificar Carrito");
        menuItemEliminarCarrito = new JMenuItem("Eliminar Carrito");
        menuCarrito.add(menuItemCrearCarrito);
        menuCarrito.add(menuItemBuscarCarrito);
        menuCarrito.add(menuItemModificarCarrito);
        menuCarrito.add(menuItemEliminarCarrito);

        menuUsuario = new JMenu("Usuarios");
        menuItemRegistrarUsuario = new JMenuItem("Registrar Usuario");
        menuItemListarUsuarios = new JMenuItem("Listar Usuarios");
        menuItemModificarUsuario = new JMenuItem("Modificar Usuario");
        menuItemEliminarUsuario = new JMenuItem("Eliminar Usuario");
        menuItemCerrarSesion = new JMenuItem("Cerrar Sesión");
        menuUsuario.add(menuItemRegistrarUsuario);
        menuUsuario.add(menuItemListarUsuarios);
        menuUsuario.add(menuItemModificarUsuario);
        menuUsuario.add(menuItemEliminarUsuario);
        menuUsuario.addSeparator();
        menuUsuario.add(menuItemCerrarSesion);

        menuIdioma = new JMenu("Idioma");
        menuItemIdiomaEspanol = new JMenuItem("Español");
        menuItemIdiomaIngles = new JMenuItem("Inglés");
        menuItemIdiomaFrances = new JMenuItem("Francés");
        menuIdioma.add(menuItemIdiomaEspanol);
        menuIdioma.add(menuItemIdiomaIngles);
        menuIdioma.add(menuItemIdiomaFrances);

        menuBar.add(menuProducto);
        menuBar.add(menuCarrito);
        menuBar.add(menuUsuario);
        menuBar.add(menuIdioma);

        setJMenuBar(menuBar);
        setContentPane(desktopPane);
        setTitle("Sistema de Carrito de Compras En Línea");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void cambiarIdioma(String lang, String country) {
        mensajeHandler.setLenguaje(lang, country);

        menuProducto.setText(mensaje("menu.producto"));
        menuCarrito.setText(mensaje("menu.carrito"));
        menuUsuario.setText(mensaje("menu.usuario"));
        menuIdioma.setText(mensaje("menu.idioma"));

        menuItemCrearProducto.setText(mensaje("producto.crear"));
        menuItemEliminarProducto.setText(mensaje("producto.eliminar"));
        menuItemActualizarProducto.setText(mensaje("producto.actualizar"));
        menuItemBuscarProducto.setText(mensaje("producto.buscar"));

        menuItemCrearCarrito.setText(mensaje("carrito.crear"));
        menuItemBuscarCarrito.setText(mensaje("carrito.buscar"));
        menuItemModificarCarrito.setText(mensaje("carrito.modificar"));
        menuItemEliminarCarrito.setText(mensaje("carrito.eliminar"));

        menuItemRegistrarUsuario.setText(mensaje("usuario.registrar"));
        menuItemListarUsuarios.setText(mensaje("usuario.listar"));
        menuItemModificarUsuario.setText(mensaje("usuario.modificar"));
        menuItemEliminarUsuario.setText(mensaje("usuario.eliminar"));
        menuItemCerrarSesion.setText(mensaje("usuario.cerrarSesion"));

        menuItemIdiomaEspanol.setText(mensaje("menu.idioma.es"));
        menuItemIdiomaIngles.setText(mensaje("menu.idioma.en"));
        menuItemIdiomaFrances.setText(mensaje("menu.idioma.fr"));

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame instanceof ec.edu.ups.vista.Producto.ProductoAñadirView p) p.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Producto.ProductoListaView p) p.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Producto.ProductoModView p) p.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Producto.ProductoElimView p) p.actualizarIdioma();

            else if (frame instanceof ec.edu.ups.vista.Carrito.CarritoAñadirView c) c.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Carrito.CarritoListaView c) c.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Carrito.CarritoModView c) c.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Carrito.CarritoElimView c) c.actualizarIdioma();

            else if (frame instanceof ec.edu.ups.vista.Usuario.UsuarioRegistroView u) u.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Usuario.UsuarioListaView u) u.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Usuario.UsuarioModView u) u.actualizarIdioma();
            else if (frame instanceof ec.edu.ups.vista.Usuario.UsuarioElimView u) u.actualizarIdioma();
        }
    }

    private String mensaje(String clave) {
        try {
            return mensajeHandler.get(clave);
        } catch (Exception e) {
            System.err.println("Error al cargar clave de idioma: " + clave);
            return clave;
        }
    }


    public JMenuItem getMenuItemCrearProducto() { return menuItemCrearProducto; }
    public JMenuItem getMenuItemEliminarProducto() { return menuItemEliminarProducto; }
    public JMenuItem getMenuItemActualizarProducto() { return menuItemActualizarProducto; }
    public JMenuItem getMenuItemBuscarProducto() { return menuItemBuscarProducto; }
    public JMenuItem getMenuItemCrearCarrito() { return menuItemCrearCarrito; }
    public JMenuItem getMenuItemBuscarCarrito() { return menuItemBuscarCarrito; }
    public JMenuItem getMenuItemModificarCarrito() { return menuItemModificarCarrito; }
    public JMenuItem getMenuItemEliminarCarrito() { return menuItemEliminarCarrito; }
    public JMenuItem getMenuItemRegistrarUsuario() { return menuItemRegistrarUsuario; }
    public JMenuItem getMenuItemListarUsuarios() { return menuItemListarUsuarios; }
    public JMenuItem getMenuItemModificarUsuario() { return menuItemModificarUsuario; }
    public JMenuItem getMenuItemEliminarUsuario() { return menuItemEliminarUsuario; }
    public JMenuItem getMenuItemCerrarSesion() { return menuItemCerrarSesion; }
    public JMenuItem getMenuItemIdiomaEspanol() { return menuItemIdiomaEspanol; }
    public JMenuItem getMenuItemIdiomaIngles() { return menuItemIdiomaIngles; }
    public JMenuItem getMenuItemIdiomaFrances() { return menuItemIdiomaFrances; }
    public JDesktopPane getDesktopPane() { return desktopPane; }

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
        getMenuItemEliminarCarrito().setEnabled(false);
    }
}
