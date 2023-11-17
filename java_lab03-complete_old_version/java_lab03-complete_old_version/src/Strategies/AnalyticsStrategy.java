package Strategies;

import Data.*;

import java.time.ZonedDateTime;
import java.util.List;
//        ● Написати метод для визначення середньої ціни всіх продуктів;
//        ● Написати метод для визначення всіх витрат заданого користувача за
//        заданий період часу;
//        ● Написати метод для отримання даних про сумарну кількість кожного
//        купленого продукту заданого користувача;
//        ● Написати метод для знаходження найпопулярнішого продукту;
//        ● Написати метод для знаходження найбільшого доходу за день.
public interface AnalyticsStrategy {
    double averagePrice(List<ProductInfo> products);
    double averagePrice();
    double costsByCustomerAndTime(Customer customer, ZonedDateTime earliestTime, ZonedDateTime latestTime);
    Item getMostPopularItem();
    double getHighestDailyProfit();

}
