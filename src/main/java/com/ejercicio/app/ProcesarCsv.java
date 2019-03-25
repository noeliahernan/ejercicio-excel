package com.ejercicio.app;

import com.ejercicio.beans.RegistrationSalesBean;
import com.ejercicio.dao.RegistrationSalesDaoImpl;
import com.ejercicio.interfaces.service.FileCsvService;
import com.ejercicio.interfaces.service.RegistrationSalesService;
import com.ejercicio.services.FileCsvServiceImpl;
import com.ejercicio.services.RegistrationSalesServiceImpl;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProcesarCsv {
    static Logger log = Logger.getLogger(ProcesarCsv.class);
        public static void main(String[] args) {
        try {
            if (args.length > 0) {
                //Inicializar el log4j
                org.apache.log4j.BasicConfigurator.configure();
                //Creamos uns instancia de los services que necesitamos
                RegistrationSalesService registrationSalesService = new RegistrationSalesServiceImpl();
                FileCsvService fileCsvService = new FileCsvServiceImpl();
                //Importamos el fichero csv y lo convertimos en una lista de objetos RegistroVenta
                List<RegistrationSalesBean> registrationSalesBeanList = fileCsvService.readExcelFile(args[0]);
                //Ordenamos la lista de registros por OrderId ascendente de menor orderId a mayor
                registrationSalesBeanList = registrationSalesBeanList.stream().sorted(Comparator.comparingLong(RegistrationSalesBean::getOrderId)).collect(Collectors.toList());
                //Insertamos en base datos
                registrationSalesService.insertRegistrationSales(registrationSalesBeanList);
                //Exportamos el excel ordenado
                fileCsvService.writeCsvFile(registrationSalesBeanList);
                //Mostramos el resumen final
                registrationSalesService.getCountRegistration();
            } else {
                log.error("Error, no se ha introducido el path de ningún fichero");
            }
        }catch(Exception e){
            log.error("Se ha producido el siguiente error:" + e.getMessage());
        }
    }


}
