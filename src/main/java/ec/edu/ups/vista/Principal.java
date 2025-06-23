// src/main/java/ec/edu/ups/vista/Principal.java
package ec.edu.ups.vista;

import javax.swing.*;

public class Principal extends JInternalFrame {
    private final JMenuItem miCrearProducto;
    private final JMenuItem miEliminarProducto;
    private final JMenuItem miActualizarProducto;
    private final JMenuItem miBuscarProducto;
    private final JMenuItem miCarrito;
    private final JMenuItem miUsuarios;
    private final JMenuItem miLogout;

    public Principal() {
        super("Sistema de Compras", true, true, true, true);

        JMenuBar mb = new JMenuBar();

        JMenu mProd = new JMenu("Producto");
        miCrearProducto      = new JMenuItem("Crear");
        miEliminarProducto   = new JMenuItem("Eliminar");
        miActualizarProducto = new JMenuItem("Actualizar");
        miBuscarProducto     = new JMenuItem("Buscar");
        mProd.add(miCrearProducto);
        mProd.add(miEliminarProducto);
        mProd.add(miActualizarProducto);
        mProd.add(miBuscarProducto);
        mb.add(mProd);

        JMenu mCar = new JMenu("Carrito");
        miCarrito = new JMenuItem("Añadir al Carrito");
        mCar.add(miCarrito);
        mb.add(mCar);

        JMenu mUser = new JMenu("Usuarios");
        miUsuarios = new JMenuItem("Gestionar Usuarios");
        miLogout   = new JMenuItem("Cerrar Sesión");
        mUser.add(miUsuarios);
        mUser.addSeparator();
        mUser.add(miLogout);
        mb.add(mUser);

        setJMenuBar(mb);
        setContentPane(new JDesktopPane());
        setSize(800, 600);
    }

    public JMenuItem getMiCrearProducto()     { return miCrearProducto; }
    public JMenuItem getMiEliminarProducto()  { return miEliminarProducto; }
    public JMenuItem getMiActualizarProducto(){ return miActualizarProducto; }
    public JMenuItem getMiBuscarProducto()    { return miBuscarProducto; }
    public JMenuItem getMiCarrito()           { return miCarrito; }
    public JMenuItem getMiUsuarios()          { return miUsuarios; }
    public JMenuItem getMiLogout()            { return miLogout; }
    public JDesktopPane getDesktop()          { return (JDesktopPane)getContentPane(); }
}
