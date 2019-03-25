package com.ejercicio.dao;

import com.ejercicio.beans.RegistrationSalesBean;
import com.ejercicio.interfaces.dao.RegistrationSalesDao;
import com.ejercicio.utils.Conexion;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RegistrationSalesDaoImpl extends Conexion implements RegistrationSalesDao {
    static Logger log = Logger.getLogger(RegistrationSalesDaoImpl.class);

    /**
     * Registra por cada bloques de 1000 registros venta en base de datos
     * @param registrationSalesBeanList
     */
    @Override
    public void insertBatch(List<RegistrationSalesBean> registrationSalesBeanList){
        log.info("Insertando registros....");
        //Recorro la lista de registros de Venta y voy realizando un isert por cada uno de ellos en la tabla REGISTRO_VENTA
        try {
            Long timeInici = System.currentTimeMillis();
            PreparedStatement ps = this.con.prepareStatement("MERGE INTO REGISTRATION_SALES KEY(ORDER_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            int batchSize = 10000;
            AtomicInteger count = new AtomicInteger();
            registrationSalesBeanList.stream().forEach(
                    data -> {
                        try {
                            int i = 1;
                            ps.setString(i++, data.getRegion());
                            ps.setString(i++, data.getCountry());
                            ps.setString(i++, data.getItemType());
                            ps.setString(i++, data.getSalesChannel());
                            ps.setString(i++, data.getOrderPriority());
                            ps.setDate(i++, Date.valueOf(data.getOrderDate()));
                            ps.setLong(i++, data.getOrderId());
                            ps.setDate(i++, Date.valueOf(data.getShipDate()));
                            ps.setLong(i++, data.getUnitsSold());
                            ps.setDouble(i++, data.getUnitsPrice());
                            ps.setDouble(i++, data.getUnitCost());
                            ps.setDouble(i++, data.getTotalRevenue());
                            ps.setDouble(i++, data.getTotalCost());
                            ps.setDouble(i++, data.getTotalProfit());
                            ps.addBatch();
                            if (count.incrementAndGet() % batchSize == 0) {
                                ps.executeBatch();
                            }
                        } catch (SQLException sql) {
                            log.error(sql.getMessage());
                        }
                    }
            );
            ps.executeBatch();
            ps.close();
            Long timeFin =System.currentTimeMillis();
            double difTime= (timeFin - timeInici)/1000;
            log.info("La insercción se ha realizado correctamente el tiempo empleado ha sido:" + difTime + " segundos");
        } catch (SQLException sql) {
            log.error(sql.getMessage());
        }

    }

    /**
     * Registra cada registro de venta de forma individual pero a través de hilos paralelos
     * @param registrationSalesBeanList
     */
    @Override
    public void insertParaleStream(List<RegistrationSalesBean> registrationSalesBeanList) {
        log.info("Insertando registros....");
        Long timeInici = System.currentTimeMillis();
        registrationSalesBeanList.parallelStream().forEach(
                data -> {
                    try {
                        PreparedStatement ps = this.con.prepareStatement("MERGE INTO REGISTRATION_SALES KEY(ORDER_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        int i = 1;
                        ps.setString(i++, data.getRegion());
                        ps.setString(i++, data.getCountry());
                        ps.setString(i++, data.getItemType());
                        ps.setString(i++, data.getSalesChannel());
                        ps.setString(i++, data.getOrderPriority());
                        ps.setDate(i++, Date.valueOf(data.getOrderDate()));
                        ps.setLong(i++, data.getOrderId());
                        ps.setDate(i++, Date.valueOf(data.getShipDate()));
                        ps.setLong(i++, data.getUnitsSold());
                        ps.setDouble(i++, data.getUnitsPrice());
                        ps.setDouble(i++, data.getUnitCost());
                        ps.setDouble(i++, data.getTotalRevenue());
                        ps.setDouble(i++, data.getTotalCost());
                        ps.setDouble(i++, data.getTotalProfit());
                        ps.executeUpdate();
                        ps.close();
                    } catch (SQLException sql) {
                        log.error("Error producido al insertar el registro con id: " + data.getOrderId() + " Error: " + sql.getMessage());
                    }
                }
        );
        Long timeFin =System.currentTimeMillis();
        double difTime= (timeFin - timeInici)/1000;
        log.info("La insercción se ha realizado correctamente el tiempo empleado ha sido:" + difTime + " segundos");
    }


    /**
     * Realiza el resumen final de conteo por el campo indicado
     * @param fieldCount
     */
    @Override
    public void count(String fieldCount) throws SQLException {
            log.info(("Preparando el resumen final..."));
            //Recorro la lista de registros de Venta y voy realizando un isert por cada uno de ellos en la tabla REGISTRO_VENTA
            PreparedStatement st = this.con.prepareStatement("SELECT " + fieldCount + ", COUNT(*)  FROM REGISTRATION_SALES GROUP BY " + fieldCount);
            ResultSet rs = st.executeQuery();
            System.out.println("<!---------RESUMEN CONTEO DEL CAMPO " + fieldCount + "------------>");
            while (rs.next()) {
                System.out.println(fieldCount + ": " + rs.getString(1) + "   TOTAL: " + rs.getString(2));
            }
            rs.close();

    }
}
