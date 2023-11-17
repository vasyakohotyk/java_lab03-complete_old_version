package Services;

import Data.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


//        Написати програму “Магазин”, яка повинна містити відомості та
//        операції для роботи продуктового магазину.
//        Вимоги до функціоналу програми:
//        ● Отримання товарів
//        ● Продаж товарів
//        ● Редагування товарів
//        ● Збереження історії покупок користувачів
//        ● Замовлення товарів
//        ● Генерація чеку замовлення у .txt форматі.
//        ● Оплачений чек неможливо редагувати.
//        ● Для роботи із файлами створити клас FileService.
//        ● Дані про кількість товарів та ціни потрібно завантажувати із файлу
//        на початку роботи програми.
//        ● Якщо у списку покупок є овочі або фрукти, для кожної позиції
//        товару необхідно додавати пакет.
//        ● Якщо у списку товарів є м’ясо або риба, необхідно згенерувати
//        коментар до чеку у форматі “Не забудьте зберігати товари {товар_1},
//        {товар_2}...{}... у холодильнику”. (Коментар повинен бути
//        англійською мовою)
//
//        ● Написати метод для отримання даних про сумарну кількість кожного
//        купленого продукту заданого користувача;
//        ● Написати метод для знаходження найпопулярнішого продукту;
//        ● Написати метод для знаходження найбільшого доходу за день.
//
//        Необхідно дотримувати принципів ООП
//        Завдання повинне бути виконане із використанням власних
//        Exception та із використанням Stream API. Якщо ж використовується
//        цикл замість Stream, на захисті потрібно пояснити чому саме цикл.
//        Самостійно визначити та створити усі класи, поля, додаткові
//        методи.
//        ● Написати метод для фільтрування та сортування усіх продуктів на
//        складі за ціною;
//        ● Написати метод для визначення середньої ціни всіх продуктів;
//        ● Написати метод для визначення всіх витрат заданого користувача за
//        заданий період часу;
public class MarketService implements Serializable {
    private List<ProductInfo> storage;
    private List<Customer> customers;

    private List<Order> orders;

    public MarketService() {
        storage = new LinkedList<>();
        customers = new LinkedList<>();
        orders = new LinkedList<>();
    }

    public ProductInfo getBag() {
        return storage.stream()
                .filter(product -> product != null &&
                        product.getItem() != null &&
                        product.getItem().getCategory() != null &&
                        product.getItem().getCategory().equals(ProductCategory.BAG)).findFirst().orElse(null);
    }

    public int getBagsCount() {
        return storage.stream()
                .filter(product -> product != null &&
                        product.getItem() != null &&
                        product.getItem().getCategory() != null &&
                        product.getItem().getCategory() == ProductCategory.BAG).mapToInt(ProductInfo::getQuantity).sum();
    }

    public void addCustomer(Customer customer) {
        if (customer == null)
            return;
        customers.add(customer);
    }

    public List<Item> getStorageItems() {
        return storage.stream().map(ProductInfo::getItem).toList();
    }

    public List<ProductInfo> getStorage() {
        return storage;
    }

    public void getInfoFromFile(String filePath, String fileName) {
        MarketService downloadMarket = FileService.deserializeMarket(filePath, fileName);
        if (downloadMarket == null)
            return;
        if (downloadMarket.storage != null && !downloadMarket.storage.isEmpty())
            storage = downloadMarket.getStorage();
        if (downloadMarket.customers != null && !downloadMarket.customers.isEmpty())
            customers = downloadMarket.customers;
        if (downloadMarket.orders != null && !downloadMarket.orders.isEmpty())
            orders = downloadMarket.orders;
    }

    public void setInfoToFile(String filePath, String fileName) {
        FileService.serializeMarket(this, filePath, fileName);
    }

    public ProductInfo createWeightProduct(String name, ProductCategory productCategory, double weight, double price) {
        if (weight < 0 || price < 0 || name == null || productCategory == null) {
            throw new IllegalArgumentException();
        }
        return new ProductInfo(new Item(name, productCategory, StorageType.KILOGRAM), 1, weight, price);
    }

    public ProductInfo createQuantityProduct(String name, ProductCategory productCategory, int quantity, int weight, double price) {
        if (weight < 0 || quantity < 0 || price < 0 || name == null || productCategory == null) {
            throw new IllegalArgumentException();
        }
        return new ProductInfo(new Item(name, productCategory, StorageType.PIECE), quantity, weight, price);
    }

    public List<ProductInfo> getProductsFromStorage(List<ProductInfo> wantedProducts) {
        return wantedProducts.stream()
                .map(product -> switch (product.getItem().getStorageType()) {
                    case KILOGRAM -> getWeightProductFromStorage(product.getItem(), product.getWeight());
                    case PIECE -> getQuantityProductFromStorage(product.getItem(), product.getQuantity());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ProductInfo getWeightProductFromStorage(Item item, double weight) {
        if (item == null || item.getStorageType() != StorageType.KILOGRAM || weight <= 0)
            return null;
        ProductInfo storageProduct = storage.stream()
                .filter(prod -> item.equals(prod.getItem())
                        && prod.getWeight() >= weight)
                .findFirst()
                .orElse(null);
        if (storageProduct == null)
            return null;
        storageProduct.pickUpWeight(weight);
        return new ProductInfo(storageProduct.getItem(), storageProduct.getQuantity(), weight, storageProduct.getPricePer());
    }

    public ProductInfo getQuantityProductFromStorage(Item item, int count) {
        if (item == null || item.getStorageType() != StorageType.PIECE || count <= 0)
            return null;
        ProductInfo storageProduct = storage.stream().filter(prod -> item.equals(prod.getItem()) && prod.getQuantity() >= count).findFirst().orElse(null);
        if (storageProduct == null)
            return null;
        storageProduct.pickUpPieces(count);
        return new ProductInfo(storageProduct.getItem(), count, storageProduct.getWeight(), storageProduct.getPricePer());
    }

    public void returnProductToStorage(ProductInfo returnedProduct) {
        if (returnedProduct == null || !returnedProduct.isValid())
            return;
        ProductInfo storageProduct = storage.stream().filter(prod -> prod.getItem().equals(returnedProduct.getItem())).findFirst().orElse(null);
        if (storageProduct == null)
            return;
        switch (storageProduct.getItem().getStorageType()) {
            case KILOGRAM -> {
                storageProduct.addWeight(returnedProduct.getWeight());
            }
            case PIECE -> {
                storageProduct.addPieces(returnedProduct.getQuantity());
            }
        }
    }

    public void returnProductsToStorage(List<ProductInfo> returnedProducts) {
        returnedProducts.stream().filter(product -> product != null && product.isValid()).forEach(this::returnProductToStorage);
    }

    public void addProductToStorage(ProductInfo product) {
        if (product == null || product.getItem() == null)
            return;
        // throw new IllegalArgumentException("Product info null!");
        ProductInfo productFromStorage = storage.stream().filter(prod ->
                        prod.getItem().equals(product.getItem()))
                .findFirst()
                .orElse(null);
        if (productFromStorage != null) {
            productFromStorage.setPricePer(product.getPrice());
            if (product.getItem().getStorageType() == StorageType.KILOGRAM)
                productFromStorage.addWeight(product.getWeight());
            if (product.getItem().getStorageType() == StorageType.PIECE)
                productFromStorage.addPieces(product.getQuantity());
        } else {
            storage.add(product);
        }
    }

    public ProductInfo getBagFromStorage() {
        ProductInfo bag = storage.stream()
                .filter(prod -> prod != null && prod.getItem() != null
                        && prod.getItem().getCategory() != null
                        && prod.getItem().getCategory() == ProductCategory.BAG)
                .findFirst()
                .orElse(null);
        if (bag == null)
            return null;
        return getQuantityProductFromStorage(bag.getItem(), 1);
    }

    public void addProductsToStorage(List<ProductInfo> products) {
        products.stream()
                .filter(product -> product != null && product.getItem() != null)
                .forEach(this::addProductToStorage);
    }

    public void addProductsToStorage(ProductInfo... products) {
        addProductsToStorage(Arrays.stream(products).toList());
    }

    private boolean storageContainsProduct(ProductInfo product) {
        return storage.stream()
                .map(ProductInfo::getItem)
                .anyMatch(storageItem -> storageItem.equals(product.getItem()));
    }


    public Order createOrder(List<ProductInfo> products) {
        List<ProductInfo> orderedProducts = getProductsFromStorage(products);
        long bagProductsCount = orderedProducts.stream()
                .filter(prod ->
                        prod.getItem().getCategory() == ProductCategory.FRUITS ||
                                prod.getItem().getCategory() == ProductCategory.VEGETABLES)
                .count();
        if (bagProductsCount > getBagsCount()) {
            System.out.println("Sorry, we don't have enough packages");
        } else {
            List<ProductInfo> bagsToAdd = IntStream.range(0, (int) bagProductsCount)
                    .mapToObj(i -> getBagFromStorage())
                    .filter(Objects::nonNull)
                    .toList();
            orderedProducts.addAll(bagsToAdd);
        }
        return new Order(LocalDateTime.now(), false, orderedProducts);
    }

    public Order createOrder(ProductInfo... products) {
        return createOrder(Arrays.stream(products).toList());
    }


    public void changeOrder(Order order, List<ProductInfo> products) {
        if (order.isPaid())
            return;
        returnProductsToStorage(products);
        order.setProducts(getProductsFromStorage(products));
    }

    public void orderToFile(Order order, String fileName, String filePath) {
        String nameWithTime = fileName + " " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm_ss")) + ".txt";
        System.out.println(order.toString());
        FileService.saveToTxt(nameWithTime, filePath, order.toString(), false);
    }

    public void payOrder(Order order) {
        if (order == null || order.isPaid())
            return;
        order.setPaid(true);
        orders.add(order);
        orderToFile(order, "Order", "orders");
    }

    public void payOrder(Order order, Customer customer) {
        if (customer == null) {
            payOrder(order);
            return;
        }
        if (order == null || order.isPaid())
            return;
        customer.addOrder(order);
        orders.add(order);
        order.setPaid(true);
        orderToFile(order, "Order", "orders");
    }


    public Order sellItems(Customer customer, List<ProductInfo> products) {
        if (customer == null || products == null || products.isEmpty())
            return null;
        Order newOrder = createOrder(products);
        payOrder(newOrder, customer);
        return newOrder;
    }


    public ProductInfo changeProductPrice(Item item, double pricePer) {
        if (item == null || pricePer <= 0)
            return null;
        ProductInfo storageProduct = storage.stream().filter(prod -> item.equals(prod.getItem())).findFirst().orElse(null);
        if (storageProduct == null)
            return null;
        storageProduct.setPricePer(pricePer);
        return storageProduct;
    }


    public void changeItem(Item item, String name, ProductCategory category, StorageType storageType) {
        if (item == null)
            return;
        ProductInfo storageProduct = storage.stream().filter(prod -> item.equals(prod.getItem())).findFirst().orElse(null);
        if (storageProduct == null || storageProduct.getItem() == null)
            return;
        if (name != null)
            storageProduct.getItem().setName(name);
        if (category != null)
            storageProduct.getItem().setCategory(category);
        if (storageType != null)
            storageProduct.getItem().setStorageType(storageType);
    }


    public List<ProductInfo> sortProductsByPrice(List<ProductInfo> goods) {
        return storage.stream()
                .sorted(Comparator.comparingDouble(ProductInfo::getPricePer))
                .toList();
    }
    public List<ProductInfo> getSortedByPriceStorageGoods() {
        return sortProductsByPrice(storage);
    }
    public List<ProductInfo> getSortedAndFilterByPriceStorageGoods(Double minPrice, Double maxPrice) {
        return storage.stream()
                .filter(prod -> prod.getPricePer()<maxPrice||prod.getPricePer()>minPrice)
                .sorted(Comparator.comparingDouble(ProductInfo::getPricePer))
                .toList();
    }

    public List<Order> getOrdersFromCustomer(Customer customer) {
        return customer.getOrders();
    }

    public double middlePrice() {
        return storage.stream()
                .mapToDouble(ProductInfo::getPricePer)
                .average()
                .orElse(0.0);
    }

    public String statisticByCustomer(Customer customer) {
        Map<Item, Double> itemsInfo = itemsByCustomer(customer);
        final String format = "%-15s|%10s %2s|\n";
        ;
        String tableHeader = String.format(format, "Item", "How much", "");

        String tableBody = itemsInfo.entrySet().stream()
                .map(entry -> {
                    Item item = entry.getKey();
                    Double value = entry.getValue();
                    String unit = item.getStorageType() == StorageType.KILOGRAM ? "kg" : "pc";
                    return String.format(format, item.getName(), value, unit);
                })
                .collect(Collectors.joining());
        return customer.toString() + "\n" + tableHeader + "------------------------------\n" + tableBody;

    }

    public Item mostPopularItem() {
        return orders.stream()
                .map(Order::getProducts)
                .flatMap(List::stream)
                .map(ProductInfo::getItem)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Map<LocalDate, Double> dayWithMaxProfit() {
        Map<LocalDate, Double> dailyProfits = profitPerDay();
        if (dailyProfits.isEmpty()) {
            return Collections.emptyMap();
        }

        Map.Entry<LocalDate, Double> maxEntry = dailyProfits.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .orElse(null);
        if (maxEntry == null || maxEntry.getKey() == null || maxEntry.getValue() == null)
            return null;
        return Collections.singletonMap(maxEntry.getKey(), maxEntry.getValue());
    }

    public String maxProfitDayToString() {
        Map<LocalDate, Double> maxProfitDay = dayWithMaxProfit();
        if (maxProfitDay.isEmpty()) {
            return ("No paid orders.");
        }
        return "Day with biggest profit: " + maxProfitDay.keySet().toString() +
                "\nProfit: " + maxProfitDay.values().toString();

    }

    public Map<LocalDate, Double> profitPerDay() {
        return orders.stream()
                .filter(Order::isPaid)
                .collect(Collectors.groupingBy(
                        order -> order.getPurchaseTime().toLocalDate(),
                        Collectors.summarizingDouble(Order::totalPaid)
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getSum()
                ));
    }

    public Map<Item, Double> itemsByCustomer(Customer customer) {
        Map<Item, Double> weight = customer.getOrders().stream()
                .flatMap(order -> order.getProducts().stream())
                .filter(prod -> prod.getItem().getStorageType() == StorageType.KILOGRAM)
                .sorted(Comparator.comparingDouble(ProductInfo::getWeight))
                .collect(Collectors.groupingBy(
                        ProductInfo::getItem,
                        Collectors.summingDouble(ProductInfo::getWeight)
                ));
        Map<Item, Double> count = customer.getOrders().stream()
                .flatMap(order -> order.getProducts().stream())
                .filter(prod -> prod.getItem().getStorageType() == StorageType.PIECE)
                .sorted(Comparator.comparingInt(ProductInfo::getQuantity))
                .collect(Collectors.groupingBy(
                        ProductInfo::getItem,
                        Collectors.summingDouble((ProductInfo::getQuantity)
                        )));
        weight.putAll(count);

        return weight;
    }

    public List<Order> ordersByCustomerAndTime(Customer customer, LocalDateTime startTime, LocalDateTime endTime) {
        return customer.getOrders().stream()
                .filter(order ->
                        order.getPurchaseTime().isBefore(endTime) &&
                                order.getPurchaseTime().isAfter(startTime)
                )
                .toList();
    }

    public double moneyByCustomerAndTime(Customer customer, LocalDateTime startTime, LocalDateTime endTime) {
        return ordersByCustomerAndTime(customer, startTime, endTime).stream().mapToDouble(Order::totalPaid).sum();
    }

    @Override
    public String toString() {
        return "MarketService{" +
                " storage=" + storage.stream().map(productInfo -> toString()) +
                ", customers=" + customers.stream().map(customer -> toString()) +
                ", orders=" + orders.stream().map(order -> toString()) +
                '}';
    }
}
