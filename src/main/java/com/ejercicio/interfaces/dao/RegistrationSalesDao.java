package com.ejercicio.interfaces.dao;

import com.ejercicio.beans.RegistrationSalesBean;

import java.sql.SQLException;
import java.util.List;

public interface RegistrationSalesDao {

    void insertBatch(List<RegistrationSalesBean> registrationSalesBeanList);
    void insertParaleStream(List<RegistrationSalesBean> registrationSalesBeanList);
    void count(String fieldCount) throws SQLException;
}
