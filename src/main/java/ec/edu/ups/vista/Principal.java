package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class Principal extends JFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JMenuBar menuBar;
    private JMenu menuProducto, menuCarrito, menuUsuario, menuIdioma;
    private JMenuItem
            menuItemCrearProducto,
            menuItemEliminarProducto,
            menuItemActualizarProducto,
            menuItemBuscarProducto,
            menuItemCrearCarrito,
            menuItemBuscarCarrito,
            menuItemModificarCarrito,
            menuItemVerDetallesCarrito,
            menuItemEliminarCarrito,
            menuItemRegistrarUsuario,
            menuItemListarUsuarios,
            menuItemModificarUsuario,
            menuItemEliminarUsuario,
            menuItemCerrarSesion,
            menuItemIdiomaEspanol,
            menuItemIdiomaIngles,
            menuItemIdiomaFrances;
    private MiJDesktopPane desktopPane;

    public Principal(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;
        initComponents();
        setTitle(mensajeHandler.get("app.titulo"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        desktopPane = new MiJDesktopPane();
        setContentPane(desktopPane);

        menuBar = new JMenuBar();

        menuProducto = new JMenu(mensajeHandler.get("menu.producto"));
        menuItemCrearProducto    = new JMenuItem(mensajeHandler.get("producto.crear"));
        menuItemEliminarProducto = new JMenuItem(mensajeHandler.get("producto.eliminar"));
        menuItemActualizarProducto = new JMenuItem(mensajeHandler.get("producto.actualizar"));
        menuItemBuscarProducto   = new JMenuItem(mensajeHandler.get("producto.buscar"));
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemActualizarProducto);
        menuProducto.add(menuItemBuscarProducto);
        menuBar.add(menuProducto);

        menuCarrito = new JMenu(mensajeHandler.get("menu.carrito"));
        menuItemCrearCarrito       = new JMenuItem(mensajeHandler.get("carrito.crear"));
        menuItemBuscarCarrito      = new JMenuItem(mensajeHandler.get("carrito.buscar"));
        menuItemModificarCarrito   = new JMenuItem(mensajeHandler.get("carrito.modificar"));
        menuItemVerDetallesCarrito = new JMenuItem(mensajeHandler.get("carrito.view.listar.detalles"));
        menuItemEliminarCarrito    = new JMenuItem(mensajeHandler.get("carrito.eliminar"));
        menuCarrito.add(menuItemCrearCarrito);
        menuCarrito.add(menuItemBuscarCarrito);
        menuCarrito.add(menuItemModificarCarrito);
        menuCarrito.add(menuItemVerDetallesCarrito);
        menuCarrito.add(menuItemEliminarCarrito);
        menuBar.add(menuCarrito);

        menuUsuario = new JMenu(mensajeHandler.get("menu.usuario"));
        menuItemRegistrarUsuario = new JMenuItem(mensajeHandler.get("usuario.registrar"));
        menuItemListarUsuarios   = new JMenuItem(mensajeHandler.get("usuario.listar"));
        menuItemModificarUsuario = new JMenuItem(mensajeHandler.get("usuario.modificar"));
        menuItemEliminarUsuario  = new JMenuItem(mensajeHandler.get("usuario.eliminar"));
        menuItemCerrarSesion     = new JMenuItem(mensajeHandler.get("usuario.cerrarSesion"));
        menuUsuario.add(menuItemRegistrarUsuario);
        menuUsuario.add(menuItemListarUsuarios);
        menuUsuario.add(menuItemModificarUsuario);
        menuUsuario.add(menuItemEliminarUsuario);
        menuUsuario.addSeparator();
        menuUsuario.add(menuItemCerrarSesion);
        menuBar.add(menuUsuario);

        menuIdioma = new JMenu(mensajeHandler.get("menu.idioma"));
        menuItemIdiomaEspanol = new JMenuItem(mensajeHandler.get("menu.idioma.es"));
        menuItemIdiomaIngles  = new JMenuItem(mensajeHandler.get("menu.idioma.en"));
        menuItemIdiomaFrances = new JMenuItem(mensajeHandler.get("menu.idioma.fr"));
        menuIdioma.add(menuItemIdiomaEspanol);
        menuIdioma.add(menuItemIdiomaIngles);
        menuIdioma.add(menuItemIdiomaFrances);
        menuBar.add(menuIdioma);

        setJMenuBar(menuBar);
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
        menuItemVerDetallesCarrito.setText(mensaje("carrito.view.listar.detalles"));
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
            try {
                Method m = frame.getClass().getMethod("actualizarIdioma");
                m.invoke(frame);
            } catch (Exception ignored) {
            }
        }
    }

    private String mensaje(String clave) {
        return mensajeHandler.get(clave);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void deshabilitarMenusAdministrador() {
        menuItemCrearProducto.setEnabled(false);
        menuItemEliminarProducto.setEnabled(false);
        menuItemActualizarProducto.setEnabled(false);
        menuItemBuscarProducto.setEnabled(false);
        menuItemCrearCarrito.setEnabled(false);
        menuItemModificarCarrito.setEnabled(false);
        menuItemVerDetallesCarrito.setEnabled(false);
        menuItemEliminarCarrito.setEnabled(false);
        menuItemRegistrarUsuario.setEnabled(false);
        menuItemModificarUsuario.setEnabled(false);
        menuItemEliminarUsuario.setEnabled(false);
    }

    public MiJDesktopPane getDesktopPane() { return desktopPane; }
    public JMenuItem getMenuItemCrearProducto() { return menuItemCrearProducto; }
    public JMenuItem getMenuItemEliminarProducto() { return menuItemEliminarProducto; }
    public JMenuItem getMenuItemActualizarProducto() { return menuItemActualizarProducto; }
    public JMenuItem getMenuItemBuscarProducto() { return menuItemBuscarProducto; }
    public JMenuItem getMenuItemCrearCarrito() { return menuItemCrearCarrito; }
    public JMenuItem getMenuItemBuscarCarrito() { return menuItemBuscarCarrito; }
    public JMenuItem getMenuItemModificarCarrito() { return menuItemModificarCarrito; }
    public JMenuItem getMenuItemVerDetallesCarrito() { return menuItemVerDetallesCarrito; }
    public JMenuItem getMenuItemEliminarCarrito() { return menuItemEliminarCarrito; }
    public JMenuItem getMenuItemRegistrarUsuario() { return menuItemRegistrarUsuario; }
    public JMenuItem getMenuItemListarUsuarios() { return menuItemListarUsuarios; }
    public JMenuItem getMenuItemModificarUsuario() { return menuItemModificarUsuario; }
    public JMenuItem getMenuItemEliminarUsuario() { return menuItemEliminarUsuario; }
    public JMenuItem getMenuItemCerrarSesion() { return menuItemCerrarSesion; }
    public JMenuItem getMenuItemIdiomaEspanol() { return menuItemIdiomaEspanol; }
    public JMenuItem getMenuItemIdiomaIngles() { return menuItemIdiomaIngles; }
    public JMenuItem getMenuItemIdiomaFrances() { return menuItemIdiomaFrances; }
}
