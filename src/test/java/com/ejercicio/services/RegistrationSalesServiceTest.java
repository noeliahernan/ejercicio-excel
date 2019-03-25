package com.ejercicio.services;

import com.ejercicio.beans.RegistrationSalesBean;
import com.ejercicio.dao.RegistrationSalesDaoImpl;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RegistrationSalesServiceTest {
    Properties p;
    RegistrationSalesDaoImpl registrationSalesDao;
    @Before
    public void  before(){
        //Leemos el path definido en el properties
        p = new Properties();
        registrationSalesDao = new RegistrationSalesDaoImpl();
        org.apache.log4j.BasicConfigurator.configure();
    }

    @Test(expected = SQLException.class)
    public void getCountRegistration() throws Exception {
        //Creamos la conexión de bd
        registrationSalesDao.conect();
        registrationSalesDao.count("PRUEBA");
        registrationSalesDao.close();
    }
    public void insertRegistrationSalesEmpty() throws Exception {
         List<RegistrationSalesBean> registrationSalesList = new ArrayList<>();
        //Creamos la conexión de bd
        registrationSalesDao.conect();
        registrationSalesDao.insertBatch(registrationSalesList);
        registrationSalesDao.close();
    }
}
