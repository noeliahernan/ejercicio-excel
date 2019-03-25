package com.ejercicio.services;

import com.ejercicio.beans.RegistrationSalesBean;
import com.ejercicio.interfaces.service.FileCsvService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileCsvServiceImpl implements FileCsvService {

    static Logger log = Logger.getLogger(FileCsvServiceImpl.class);

    /**
     * Método que se encarga de leer el fichero csv del path introducido
     * @param csvPath
     * @return
     */
    @Override
    public List<RegistrationSalesBean> readExcelFile(String csvPath) {
        log.info("Comenzando a leer el fichero....");
        //Creamos una lista de registroVentas que cargaremos con los registros del csv
        List<RegistrationSalesBean> registrationSalesBeanList = new ArrayList<>();

        try {
            //Creamos un BufferedREader para el archivo que queremos leer
            Reader reader = Files.newBufferedReader(Paths.get(csvPath));

            //Le pasamos un parser con el formato por defecto y le aplicamos varíos métodos de formato para sacar la información
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    //Usa el primer registro como encabezado
                    .withFirstRecordAsHeader()
                    //Para acceder a los nombres de las cabeceras no se tienen en cuenta si están en minúscula o mayúscula
                    .withIgnoreHeaderCase()
                    //Elimina espacios en blancos iniciales o finales.
                    .withTrim());
            //Spliterator se puede usar para dividir un determinado conjunto de elementos en varios conjuntos para poder realizar operaciones
            //el false es que no quiero usar el paralelismo que es para un tema de hilos, y así lo uso de forma secuencial
            //Utilizamos el StreamSupport para convertir la instancia de iterable en Stream para poder utilizar los métodos de stream.
            Stream<CSVRecord> csvRecordStream = StreamSupport.stream(csvParser.spliterator(), false);
            registrationSalesBeanList = csvRecordStream
                    //Devuelve un stream en el que se descarta la cabecera, ya que es el primer registro
                    .skip(1)
                    //El map se utiliza para transformar la información
                    .map(csvRecord -> csvRecord.toMap())
                    .map(RegistrationSalesBean::new)
                    //Genera el resultado de un stream como una lista
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Se ha producido el siguiente error leyendo el fichero csv: " + e.getMessage());

        }
        log.info("Finalizada correctamente la lectura del fichero");
        return registrationSalesBeanList;
    }


    /**
     * Método que se encarga de escribir el nuevo csv y almacenarlo en el path indiccado
     * @param registrationSalesBeanList
     */
    @Override
    public void writeCsvFile(List<RegistrationSalesBean> registrationSalesBeanList) {
        log.info("Comenzando a escribir el fichero...");
        try {
            //Leemos el path definido en el properties
            Properties p = new Properties();
            p.load(new FileReader("src/properties/config.properties"));

            //Creamos un buffer para escribir el fichero
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(p.getProperty("pathNuevoCsv")));

            //Indicamos las cabeceras del fichero y recorremos la lista de registro para escribir cada registro
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("Region", "Country", "ItemType","Sales Channel","Order Priority","Order Date","Order ID"));
            registrationSalesBeanList.forEach(registrationSalesBean -> {
                try {
                    // Formateamos la fecha
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                    //Pintamos el registro
                    csvPrinter.printRecord(registrationSalesBean.getRegion(), registrationSalesBean.getCountry(), registrationSalesBean.getItemType(), registrationSalesBean.getSalesChannel(),
                            registrationSalesBean.getOrderPriority(), formatter.format(registrationSalesBean.getOrderDate()), registrationSalesBean.getOrderId(), formatter.format(registrationSalesBean.getShipDate()),
                            registrationSalesBean.getUnitsSold(), registrationSalesBean.getUnitsPrice(), registrationSalesBean.getUnitCost(), registrationSalesBean.getTotalRevenue(), registrationSalesBean.getTotalCost(), registrationSalesBean.getTotalProfit());
                } catch (IOException e) {
                    log.error("Se ha producido el siguiente error escribiendo el fichero csv: " + e.getMessage());
                }
            });
            //Obligamos al buffer a escribir el fichero.
            csvPrinter.flush();
            writer.flush();
            log.info("Escritura del fichero finalizada correctamente");
        } catch (IOException e) {
            log.error("Se ha producido el siguiente error escribiendo el fichero csv: " + e.getMessage());
        }
    }
}
