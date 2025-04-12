package org.example.factory;

import org.example.DAO.ClienteDAO;
import org.example.DAO.FacturaDAO;
import org.example.DAO.Factura_ProductoDAO;
import org.example.DAO.ProductoDAO;
import org.example.db.Conexion;

import java.sql.Connection;

public class MySQLDAOFactory extends AbstractFactory {
    private static MySQLDAOFactory instance = null;
    private final Connection conn;

    private MySQLDAOFactory() {
        this.conn = Conexion.getInstancia().getConnection(); // ✅ USO del Singleton
    }

    public static synchronized MySQLDAOFactory getInstance() {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    @Override
    public ClienteDAO getClienteDAO() {
        return new ClienteDAO(conn);
    }

    @Override
    public Factura_ProductoDAO getFactura_ProductoDAO() {
        return new Factura_ProductoDAO(conn);
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return new FacturaDAO(conn);
    }

    @Override
    public ProductoDAO getProductoDAO() {
        return new ProductoDAO(conn);
    }

    public void cerrarConexion() {
        Conexion.getInstancia().cerrarConexion();
    }
}
