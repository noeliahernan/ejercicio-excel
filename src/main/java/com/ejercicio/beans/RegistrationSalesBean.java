package com.ejercicio.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RegistrationSalesBean {
    String region;
    String country;
    String itemType;
    String salesChannel;
    String orderPriority;
    LocalDate orderDate;
    Long orderId;
    LocalDate shipDate;
    Long unitsSold;
    Double unitsPrice;
    Double unitCost;
    Double totalRevenue;
    Double totalCost;
    Double totalProfit;

    public RegistrationSalesBean(Map <String, String> row) {
        //Creamos el formato en el que viene la fecha en el csv
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        this.region = row.get("Region");
        this.country = row.get("Country");
        this.itemType = row.get("Item Type");
        this.salesChannel = row.get("Sales Channel");
        this.orderPriority = row.get("Order Priority");
        this.orderDate = LocalDate.parse(row.get("Order Date"),formatter);
        this.orderId = Long.parseLong(row.get("Order ID"));
        this.shipDate = LocalDate.parse(row.get("Ship Date"),formatter);
        this.unitsSold = Long.parseLong(row.get("Units Sold"));
        this.unitsPrice = Double.parseDouble(row.get("Unit Price"));
        this.unitCost = Double.parseDouble(row.get("Unit Cost"));
        this.totalRevenue = Double.parseDouble(row.get("Total Revenue"));
        this.totalCost = Double.parseDouble(row.get("Total Cost"));
        this.totalProfit = Double.parseDouble(row.get("Total Profit"));
    }
    public RegistrationSalesBean() {

    }
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDate shipDate) {
        this.shipDate = shipDate;
    }

    public Long getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(Long unitsSold) {
        this.unitsSold = unitsSold;
    }

    public Double getUnitsPrice() {
        return unitsPrice;
    }

    public void setUnitsPrice(Double unitsPrice) {
        this.unitsPrice = unitsPrice;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(Double totalProfit) {
        this.totalProfit = totalProfit;
    }
}
