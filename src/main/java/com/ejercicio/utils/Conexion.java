package com.ejercicio.utils;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    protected Connection con;

    static Logger log = Logger.getLogger(Connection.class);

    /**
     * Método que se encarga de crear la conexión a base de datos
     * @throws Exception
     */
    public void conect() throws  Exception{
        log.info("Creando conexión y conectando...");
        try {
            Properties p = new Properties();
            p.load(new FileReader("src/properties/config.properties"));

            con = DriverManager.getConnection(p.getProperty("db_url"), p.getProperty("user"), p.getProperty("password"));
          Class.forName(p.getProperty("jdbc_driver"));
         log.info("Conexión creada correctamente");
        }catch (Exception e){
            log.error("Se ha producido el siguiente error creando la conexión: " + e.getMessage());
        }
    }

    /**
     * Método encargado de cerrar las conexiones abiertas
     * @throws SQLException
     */
    public void close() throws SQLException{
        if(con != null){
            if(!con.isClosed()){
                con.close();
                log.info("Conexion cerrada correctamente");
            }
        }
    }
}
