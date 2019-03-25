package com.ejercicio.interfaces.service;

import com.ejercicio.beans.RegistrationSalesBean;

import java.util.List;

public interface FileCsvService {
    List<RegistrationSalesBean> readExcelFile(String csvPath);
    void writeCsvFile(List<RegistrationSalesBean> registrationSalesBeanList);
}
