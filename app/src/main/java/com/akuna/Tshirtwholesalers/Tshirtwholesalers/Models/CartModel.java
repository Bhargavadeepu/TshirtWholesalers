package com.akuna.Tshirtwholesalers.Tshirtwholesalers.Models;

public class CartModel {




    int CartId;
    String ProductID;
    String VariantID;
    String Size;
    String Color;
    String ImageSrc;
    String AvailbleforSale;
    String Quantity;
    String Title;
    String Discount;
    String Price;
    double totalPrice;
    double ComparaterPrice;
    String descripation;

    public String getDescripation() {
        return descripation;
    }

    public void setDescripation(String descripation) {
        this.descripation = descripation;
    }

    public double getComparaterPrice() {
        return ComparaterPrice;
    }

    public void setComparaterPrice(double comparaterPrice) {
        ComparaterPrice = comparaterPrice;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCartId() {
        return CartId;
    }

    public void setCartId(int cartId) {
        CartId = cartId;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getVariantID() {
        return VariantID;
    }

    public void setVariantID(String variantID) {
        VariantID = variantID;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getImageSrc() {
        return ImageSrc;
    }

    public void setImageSrc(String imageSrc) {
        ImageSrc = imageSrc;
    }

    public String getAvailbleforSale() {
        return AvailbleforSale;
    }

    public void setAvailbleforSale(String availbleforSale) {
        AvailbleforSale = availbleforSale;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }







}
