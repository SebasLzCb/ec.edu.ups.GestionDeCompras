package ec.edu.ups.vista;

import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {

            ProductoView productoView = new ProductoView();
            ProductoListaView productoListaView = new ProductoListaView();
            ProductoDAO productoDAO = new ProductoDAOMemoria();
            new ProductoController(productoView, productoDAO, productoListaView);
        });
    }
}
