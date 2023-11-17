package Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Order implements Serializable {
    private LocalDateTime purchaseTime;
    private List<ProductInfo> products = new ArrayList<>();
    private boolean isPaid;

    public Order(LocalDateTime purchaseTime, boolean isPaid, List<ProductInfo> products) {
        this.purchaseTime = purchaseTime;
        this.products = products;
        this.isPaid = isPaid;
    }

    public Order(LocalDateTime purchaseTime, boolean isPaid, ProductInfo... products) {
        this.purchaseTime = purchaseTime;
        this.products = Arrays.stream(products).filter(Objects::nonNull).toList();
        this.isPaid = isPaid;

    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInfo> products) {
        this.products = products;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public double totalPaid() {
        return BigDecimal.valueOf(products.stream().mapToDouble(ProductInfo::getPrice).sum()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public String comment() {
        if (this.products == null)
            return "";

        List<ProductInfo> filteredProducts = this.products.stream()
                .filter(Objects::nonNull)
                .filter(prod ->
                        prod.getItem().getCategory() == ProductCategory.MEAT ||
                                prod.getItem().getCategory() == ProductCategory.FISH)
                .toList();

        if (filteredProducts.isEmpty())
            return "";

        return "Don't forget to keep the " +
                filteredProducts.stream()
                        .map(ProductInfo::getShortString)
                        .collect(Collectors.joining(", "))
                + " in the refrigerator";
    }

    @Override
    public String toString() {
        return purchaseTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + "\n" +
                products.stream().map(ProductInfo::toString).collect(Collectors.joining("\n")) +
                "\n Total paid: " + totalPaid() + "\n" + comment();
    }
}
