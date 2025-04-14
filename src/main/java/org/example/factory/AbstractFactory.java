package org.example.factory;

import org.example.DAO.ClienteDAO;
import org.example.DAO.FacturaDAO;
import org.example.DAO.Factura_ProductoDAO;
import org.example.DAO.ProductoDAO;

public abstract class AbstractFactory {

    // Métodos que toda fábrica concreta debe implementar
    public abstract ClienteDAO getClienteDAO();
    public abstract Factura_ProductoDAO getFactura_ProductoDAO();
    public abstract FacturaDAO getFacturaDAO();
    public abstract ProductoDAO getProductoDAO();

    // Esta implementación devuelve directamente la fábrica de MySQL,
    // ya que sólo se trabaja con ese motor de base de datos en este proyecto.
    public static AbstractFactory getDAOFactory() {
        return MySQLDAOFactory.getInstance();
    }
}
// El metodo getDAOFactory() implementa el patrón Abstract Factory, que encapsula la creación
//  de los objetos DAO. Retorna una única instancia de MySQLDAOFactory, que a su vez utiliza
//  el patrón Singleton para asegurar que se use una única conexión (Connection)
//  a la base de datos durante toda la ejecución del programa.