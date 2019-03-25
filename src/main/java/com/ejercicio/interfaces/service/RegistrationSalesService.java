package com.ejercicio.interfaces.service;

import com.ejercicio.beans.RegistrationSalesBean;

import java.sql.SQLException;
import java.util.List;

public interface RegistrationSalesService {
    void insertRegistrationSales(List<RegistrationSalesBean> registroVentaList) throws Exception;
    void getCountRegistration() throws SQLException;
}
