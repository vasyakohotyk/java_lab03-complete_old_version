import Data.*;
import Services.MarketService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
//        ● Написати метод для фільтрування та сортування усіх продуктів на
//        складі за ціною;
//        ● Написати метод для визначення середньої ціни всіх продуктів;
//        ● Написати метод для визначення всіх витрат заданого користувача за
//        заданий період часу;
//        ● Написати метод для отримання даних про сумарну кількість кожного
//        купленого продукту заданого користувача;
//        ● Написати метод для знаходження найпопулярнішого продукту;
//        ● Написати метод для знаходження найбільшого доходу за день.
//        Необхідно дотримувати принципів ООП
//        Завдання повинне бути виконане із використанням власних
//        Exception та із використанням Stream API. Якщо ж використовується
//        цикл замість Stream, на захисті потрібно пояснити чому саме цикл.
//        Самостійно визначити та створити усі класи, поля, додаткові
//        методи.
public class Main {
    public static void main(final String[] args) throws InterruptedException {

        final MarketService service = new MarketService();
//      ● Дані про кількість товарів та ціни потрібно завантажувати із файлу
//      на початку роботи програми.
        service.getInfoFromFile("Service data", "Service data");
        Item catFoodItem = new Item("Cat food", ProductCategory.PET, StorageType.KILOGRAM);
        Item meatItem = new Item("Meat", ProductCategory.MEAT, StorageType.KILOGRAM);
        Item potatoItem = new Item("Potato", ProductCategory.VEGETABLES, StorageType.KILOGRAM);
        Item bagItem = new Item("Bag", ProductCategory.BAG, StorageType.PIECE);
        Item applesItem = new Item("Green apple", ProductCategory.FRUITS, StorageType.PIECE);
        Item coffeeItem = new Item("Coffee", ProductCategory.COFFEE_TEA, StorageType.PIECE);

        ProductInfo catFood = new ProductInfo(catFoodItem, 1, 120, 14.);
        ProductInfo meat = new ProductInfo(meatItem, 1, 300., 18.);
        ProductInfo potato = new ProductInfo(potatoItem, 1, 100., 18.);
        ProductInfo bag = new ProductInfo(bagItem, 100, 0.04, 2.5);
        ProductInfo apples = new ProductInfo(applesItem, 280, 0.4, 4.);
        ProductInfo coffee = new ProductInfo(coffeeItem, 190, 0.8, 235.);
        Customer customer = new Customer("Patric", "Sponge");

//        service.addCustomer(customer);
//        service.addProductsToStorage(catFood, meat, potato, bag, apples, coffee);
//        service.setInfoToFile("Service data", "Service data");

        //● Отримання товарів
        System.out.println(service.getStorageItems().stream().map(Item::toString).collect(Collectors.toList()));

        //● Замовлення товарів
        //● Продаж товарів
        Order testOrder = service.createOrder(
                new ProductInfo(potatoItem, 1, 10, 12.)
                , new ProductInfo(meatItem, 1, 5, 10.)
                , new ProductInfo(coffeeItem, 1, 1., 1.));
        service.payOrder(testOrder, customer);
        Thread.sleep(3000);
        Order testOrder2 = service.createOrder(new ProductInfo(potatoItem, 12, 11.,1.));
        service.payOrder(testOrder2, customer);
        //● Редагування товарів
        System.out.println("Редагування товару");
        service.changeItem(applesItem, "Red apples", null, null);
        System.out.println(service.getStorageItems().stream().map(Item::toString).collect(Collectors.toList()));

        //● Збереження історії покупок користувачів
        System.out.println("Збереження історії покупок користувачів");
        System.out.println(service.statisticByCustomer(customer));

//      ● Написати метод для фільтрування та сортування усіх продуктів на
//      складі за ціною;
        System.out.println("\nСортування за ціною");
        System.out.println(service.getSortedAndFilterByPriceStorageGoods(5., 50.));
//        ● Написати метод для визначення середньої ціни всіх продуктів;
        System.out.println("Середня ціна продуктів");
        System.out.println(service.middlePrice());
//        ● Написати метод для визначення всіх витрат заданого користувача за
//        заданий період часу;
        System.out.println("Написати метод для визначення всіх витрат заданого користувача за заданий період часу");
        System.out.println(service.ordersByCustomerAndTime(customer, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
//        ● Написати метод для отримання даних про сумарну кількість кожного
//        купленого продукту заданого користувача;
        System.out.println("Дані про сумарну кількість кожного купленого продукту");
        System.out.println(service.statisticByCustomer(customer));
//        ● Написати метод для знаходження найпопулярнішого продукту;
        System.out.println("Найпопулярніший продукт");
        System.out.println(service.mostPopularItem());
//        ● Написати метод для знаходження найбільшого доходу за день.
        System.out.println("Biggest profit per day");
        System.out.println(service.maxProfitDayToString());
//        Необхідно дотримувати принципів ООП
    }
}


//    Item catFoodItem = new Item("Cat food", ProductCategory.PET, StorageType.KILOGRAM);
//    Item meatItem = new Item("Meat", ProductCategory.MEAT, StorageType.KILOGRAM);
//    Item potatoItem = new Item("Potato", ProductCategory.VEGETABLES, StorageType.KILOGRAM);
//    Item bagItem = new Item("Bag", ProductCategory.BAG, StorageType.PIECE);
//    Item applesItem = new Item("Green apple", ProductCategory.FRUITS, StorageType.PIECE);
//    Item coffeeItem = new Item("Coffee", ProductCategory.COFFEE_TEA, StorageType.PIECE);
//
//    ProductInfo catFood = new ProductInfo(catFoodItem, 1, 12, 14.);
//    ProductInfo meat = new ProductInfo(meatItem, 1, 30., 180.);
//    ProductInfo potato = new ProductInfo(potatoItem, 1, 100., 180.);
//    ProductInfo bag = new ProductInfo(bagItem, 100, 0.04, 2.5);
//    ProductInfo apples = new ProductInfo(applesItem, 28, 0.4, 4.);
//    ProductInfo coffee = new ProductInfo(coffeeItem, 19, 0.8, 235.);
//    Customer customer = new Customer("Patric", "Sponge");
//
