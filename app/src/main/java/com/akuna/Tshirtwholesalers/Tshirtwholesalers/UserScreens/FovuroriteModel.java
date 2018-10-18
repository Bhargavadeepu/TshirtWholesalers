package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

public class FovuroriteModel {

    int FavId;
    String ProductID;
    String VariantID;
    String Size;
    String Color;
    String ImageSrc;
    String AvailbleforSale;
    double Quantity;
    String Title;
    double Discount;
    double Price;
    double totalPrice;
    double ComparaterPrice;
    String descripation;

    public double getComparaterPrice() {
        return ComparaterPrice;
    }

    public void setComparaterPrice(double comparaterPrice) {
        ComparaterPrice = comparaterPrice;
    }

    public String getDescripation() {
        return descripation;
    }

    public void setDescripation(String descripation) {
        this.descripation = descripation;
    }
    public int getFavId() {
        return FavId;
    }

    public void setFavId(int favId) {
        FavId = favId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }



}
