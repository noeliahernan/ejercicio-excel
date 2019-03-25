package com.ejercicio.services;

import com.ejercicio.beans.RegistrationSalesBean;
import com.ejercicio.dao.RegistrationSalesDaoImpl;
import com.ejercicio.interfaces.service.RegistrationSalesService;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.util.List;
import java.util.Properties;


public class RegistrationSalesServiceImpl implements RegistrationSalesService {

    private static final String REGION = "REGION";
    private static final String COUNTRY = "COUNTRY";
    private static final String ITEM_TYPE = "ITEM_TYPE";
    private static final String SALES_CHANNE = "SALES_CHANNE";
    private static final String ORDER_PRIORITY = "ORDER_PRIORITY";
    private static final String BATCH = "BATCH";
    static Logger log = Logger.getLogger(RegistrationSalesDaoImpl.class);

    /**
     * Método que llama a la capa de persisitencia para insertar los registros
     * @param registrationSalesList
     * @throws Exception
     */
    @Override
    public void insertRegistrationSales(List<RegistrationSalesBean> registrationSalesList) throws Exception {
        RegistrationSalesDaoImpl registrationSalesDao = new RegistrationSalesDaoImpl();
        Properties p = new Properties();
        try {
            p.load(new FileReader("src/properties/config.properties"));
            //Creamos la conexión de bd
            registrationSalesDao.conect();
            //Si el modo es batch lanzamos batch y si no lanzamos parallel
            if (p.getProperty("modeInsert").equals(BATCH))
                registrationSalesDao.insertBatch(registrationSalesList);
            else
                registrationSalesDao.insertParaleStream(registrationSalesList);
        } catch (Exception e) {
            throw e;
        } finally {
            registrationSalesDao.close();
        }
    }


    /**
     * Método que llama a la capa de persistencia para realizar el conteo enviandole el campo
     */
    @Override
    public void getCountRegistration() {
        try {
            RegistrationSalesDaoImpl registrationSalesDao = new RegistrationSalesDaoImpl();
            //Creamos la conexión de bd
            registrationSalesDao.conect();
            registrationSalesDao.count(REGION);
            registrationSalesDao.count(COUNTRY);
            registrationSalesDao.count(ITEM_TYPE);
            registrationSalesDao.count(SALES_CHANNE);
            registrationSalesDao.count(ORDER_PRIORITY);
            //Cerramos conexión de bd
            registrationSalesDao.close();
        } catch (Exception e) {
            log.error("Se ha producido el siguiente error mostrando el resumen: " + e.getMessage());
        }
    }

}
