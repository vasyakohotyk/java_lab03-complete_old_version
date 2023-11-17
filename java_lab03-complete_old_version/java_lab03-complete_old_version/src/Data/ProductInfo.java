package Data;

import Services.IdService;

import java.io.Serializable;

public class ProductInfo implements Serializable {
    private Item item;
    private int quantity;
    private double weight;
    private Double pricePer;

    public ProductInfo(Item item, int quantity, double weight, Double pricePer) {
        this.item = item;
        this.quantity = quantity;
        this.weight = weight;
        this.pricePer = pricePer;
    }

    public Double getPricePer() {
        return pricePer;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void addWeight(double weight){
        if(weight>0)
            this.weight+=weight;
    }
    public void addPieces(double pieces){
        if(pieces>0)
            this.quantity+=pieces;
    }
    public void pickUpWeight(double weight){
        if(weight>0)
            this.weight-=weight;
    }
    public void pickUpPieces(double pieces){
        if(pieces>0)
            this.quantity-=pieces;
    }
    public Double getPrice() {
        if(pricePer ==null)
            return null;
        switch (item.getStorageType()){
            case PIECE -> {
                return pricePer * quantity;
            }
            case KILOGRAM -> {
                return pricePer * weight;
            }
            default -> {
                return null;
            }
        }
    }
    public void setPricePer(double price) {
        this.pricePer = price;
    }
    public boolean isValid(){
        if(item==null||item.getName()==null||pricePer==null)
            return false;
        switch (item.getStorageType()) {
            case PIECE -> {
                if(quantity<=0)
                    return false;
            }
            case KILOGRAM -> {
                if(weight<=0)
                    return false;
            }
            default -> {
                return false;
            }
        }
        return true;
    }
    public String getShortString(){
        return item.getName();
    }
    @Override
    public String toString() {
        String string = item.toString()+ "\n" ;
        switch (item.getStorageType()){
            case KILOGRAM ->
            {
                string += " Price per kg: " + pricePer;
                string += "\n Weight: " + weight;
            }
            case PIECE -> {
                string+= "Price per one: " + pricePer;
                string+= "\n Count: " + quantity;
            }
            default -> {
                string+= "no price";
            }
        }
        return string ;
    }
}
