package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.ProductoListaView;
import ec.edu.ups.vista.ProductoView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductoController {

    private final ProductoView productoView;
    private final ProductoListaView productoListaView;
    private final ProductoDAO productoDAO;

    /**
     * Constructor principal: recibe la vista de alta, el DAO y la vista de listado.
     */
    public ProductoController(ProductoView productoView,
                              ProductoDAO productoDAO,
                              ProductoListaView productoListaView) {
        this.productoView      = productoView;
        this.productoDAO       = productoDAO;
        this.productoListaView = productoListaView;
        configurarEventos();
        // Carga inicial del listado
        productoListaView.mostrarProductos(productoDAO.listarTodos());
    }

    /**
     * Configura el listener del botón "Aceptar" de la vista de alta.
     */
    private void configurarEventos() {
        productoView.getBtnAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });
    }

    /**
     * Lee los campos de texto, crea el Producto y llama al DAO.
     * Luego limpia la vista de alta y actualiza la de listado.
     */
    private void guardarProducto() {
        try {
            int codigo = Integer.parseInt(productoView.getTxtCodigo().getText().trim());
            String nombre = productoView.getTxtNombre().getText().trim();
            double precio = Double.parseDouble(productoView.getTxtPrecio().getText().trim());

            Producto p = new Producto(codigo, nombre, precio);
            productoDAO.crear(p);

            productoView.limpiarCampos();
            productoListaView.mostrarMensaje("Producto guardado correctamente");
            productoListaView.mostrarProductos(productoDAO.listarTodos());

        } catch (NumberFormatException ex) {
            productoView.mostrarMensaje("Error: ingresa valores numéricos válidos.");
        }
    }
}
