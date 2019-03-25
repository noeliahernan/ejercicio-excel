package com.ejercicio.services;

import com.ejercicio.beans.RegistrationSalesBean;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FileCsvServiceTest {
    Properties p;
    List<RegistrationSalesBean> registrationSalesBeanList;
    @Before
    public void  before(){
        //Leemos el path definido en el properties
        p = new Properties();
        registrationSalesBeanList = new ArrayList<RegistrationSalesBean>();
        org.apache.log4j.BasicConfigurator.configure();
    }
    @Test
    public void testReaderNotNull() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get("files/RegistroVentas1.csv"));
        assertNotNull(reader);
    }

    @Test
    public void testReaderRecordNotEmpty() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get("files/RegistroVentas1.csv"));
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

        assertTrue(registrationSalesBeanList.size() > 0);
    }

    @Test
    public void loadProperties() throws IOException {
        this.p.load(new FileReader("src/properties/config.properties"));
        assertNotNull(p.getProperty("pathNuevoCsv"));
    }

    @Test
    public void testWriterNotNull() throws IOException {
        this.p.load(new FileReader("src/properties/config.properties"));

        //Creamos un buffer para escribir el fichero
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(p.getProperty("pathNuevoCsv")));
        assertNotNull(writer);
    }

    @Test
    public void writerCsv()  throws IOException {
        //Leemos el path definido en el properties
        p.load(new FileReader("src/properties/config.properties"));

        //Creamos un buffer para escribir el fichero

        StringWriter sw = new StringWriter();


        //Indicamos las cabeceras del fichero y recorremos la lista de registro para escribir cada registro
        CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                .withHeader("Region", "Country", "ItemType","Sales Channel","Order Priority","Order Date","Order ID"));

        assertNotNull(csvPrinter);
        RegistrationSalesBean registrationSalesBean = new RegistrationSalesBean();
        registrationSalesBean.setCountry("ESPAÑA");
        registrationSalesBean.setRegion("TEST");
        registrationSalesBean.setOrderId(2L);
        registrationSalesBean.setOrderDate(LocalDate.now());
        registrationSalesBean.setShipDate(LocalDate.now());
        registrationSalesBeanList.add(registrationSalesBean);
        registrationSalesBeanList.forEach(registrationSales -> {
            try {
                // Formateamos la fecha
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                //Pintamos el registro
                csvPrinter.printRecord(registrationSales.getRegion(), registrationSales.getCountry(), registrationSales.getItemType(), registrationSales.getSalesChannel(),
                        registrationSales.getOrderPriority(), formatter.format(registrationSales.getOrderDate()), registrationSales.getOrderId(), formatter.format(registrationSales.getShipDate()),
                        registrationSales.getUnitsSold(), registrationSales.getUnitsPrice(), registrationSales.getUnitCost(), registrationSales.getTotalRevenue(), registrationSales.getTotalCost(), registrationSalesBean.getTotalProfit());
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        //Obligamos al buffer a escribir el fichero.
        sw.flush();
        System.out.println(sw.toString());
        assertTrue(sw.toString().contains("ESPAÑA"));
        assertNotEquals(sw.toString(),"");
    }
}
