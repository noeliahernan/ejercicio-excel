package com.ejercicio.utils;

import com.ejercicio.dao.RegistrationSalesDaoImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConexionTest {
    Properties p;
    RegistrationSalesDaoImpl registrationSalesDao;
    @Before
    public void  before(){
        //Leemos el path definido en el properties
        p = new Properties();
    }

    @Test
    public void crearConexion() throws Exception {
        p.load(new FileReader("src/properties/config.properties"));
        Connection con = DriverManager.getConnection(p.getProperty("db_url"), p.getProperty("user"), p.getProperty("password"));
        assertNotNull(con);
    }

    @Test
    public void pathProperties() throws Exception {
        p.load(new FileReader("src/properties/config.properties"));
        assertNotNull(p.propertyNames());
    }

    @Test
    public void loadPropertiesCon() throws  Exception {
        p.load(new FileReader("src/properties/config.properties"));
        assertNotNull(p.getProperty("db_url"));
        assertNotNull(p.getProperty("user"));
        assertNotNull(p.getProperty("password"));
    }
    @Test
    public void closeCon() throws  Exception {
        p.load(new FileReader("src/properties/config.properties"));
        Connection con = DriverManager.getConnection(p.getProperty("db_url"), p.getProperty("user"), p.getProperty("password"));
        con.close();
        assertTrue(con.isClosed());
    }
}
