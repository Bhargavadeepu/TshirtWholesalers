package com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models;

import java.math.BigDecimal;

public class CollectionModel {


    String image;
    String name;
    BigDecimal cost;
    String productId;
    String variantId;
    String descripation;
    String title;
    String descripationHtml;
    BigDecimal compareatprice;
    String availableSale;
    BigDecimal discount;

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getAvailableSale() {
        return availableSale;
    }
    public void setAvailableSale(String availableSale) {
        this.availableSale = availableSale;
    }

    public BigDecimal getCompareatprice() {
        return compareatprice;
    }
    public void setCompareatprice(BigDecimal compareatprice) {
        this.compareatprice = compareatprice;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescripation() {
        return descripation;
    }

    public void setDescripation(String descripation) {
        this.descripation = descripation;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }


}
