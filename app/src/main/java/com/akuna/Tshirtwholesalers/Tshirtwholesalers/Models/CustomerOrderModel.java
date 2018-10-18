package com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models;

public class CustomerOrderModel {

    String OrderId;
    String TotalPrice;
    String Url;
    String orderDate;
    String Title;
    String imagSrc;

    public String getImagSrc() {
        return imagSrc;
    }

    public void setImagSrc(String imagSrc) {
        this.imagSrc = imagSrc;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }




}
