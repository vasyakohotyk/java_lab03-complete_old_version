package Data;

import Services.IdService;

import java.io.Serializable;
import java.util.Objects;

public class Item implements Serializable {
    private String name;
    private ProductCategory category;
    private StorageType storageType;

    public Item(String name, ProductCategory category, StorageType storageType) {
        this.name = name;
        this.category = category;
        this.storageType = storageType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }
    @Override
    public String toString() {
        return name +" (" + category.toString() + ") ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && category == item.category && storageType == item.storageType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, storageType);
    }
}

