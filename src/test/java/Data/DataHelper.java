package Data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class DataHelper {
    //решил в самом начале сгенерировать данные, чтобы цпростить работу
    //всеравно дата - это текущяя дата, ее можно сгенерировать только 1 раз, а не при каждом запросе
    //и достаточно использовать одно имя для всех тестов. кроме тесто на пустое поле имени или невалидных даннх
    private static final DateTimeFormatter YearFormat = DateTimeFormatter.ofPattern("YY");
    private static final DateTimeFormatter MonthFormat = DateTimeFormatter.ofPattern("MM");
    private static final Faker faker = new Faker();

    private DataHelper() {
    }

    //решил не усложнять себе жизнь и сделоть возможность добавить n для месяца и m года.
    //если для года - с этим все понятно. Для месяца - это для тестов невалидных данных
    //к примеру, добавить еще 10 месяцев к текущей дате - явно выходит за рамки 12
    public static String getDateYear(int addedYears) {
        return LocalDate.now().plusYears(addedYears).format(YearFormat);
    }

    public static String getDateMonth(int addedMonths) {
        return LocalDate.now().plusMonths(addedMonths).format(MonthFormat);
    }

    //тут я не совсем уверен. Нужно полное имя? Если да, то как записывать? Через пробел?
    //поэтому решил не усложнять себе жизнь и использую генерацию только имени, без фамилии
    public static String getName() {
        return faker.name().firstName();
    }

    //Для CVV вполне сойдет рандомайзер на трезначное число (100-999)
    public static String getCvv() {
        return String
                .valueOf(100 + ThreadLocalRandom.current().nextInt(900));
    }

    public static String getCardNumber(boolean isFirstCard) {
        //Возможно, было бы лучше использовать ENUM для карт (это дало бы масштабируемость списка карт)
        //но я решил использовать именно boolean, т.к. проще запомнить (true - одобрено, false - отказано)
        //это я про данные, которые данны в data.json
        return isFirstCard ? "4444444444444441" : "4444444444444442";
    }


    public static CardInfo generateCard(int addedYears, int addedMonths, boolean isFirstCard) {
        return new CardInfo(getName(), getCvv(), getCardNumber(isFirstCard)
                , getDateYear(addedYears)
                , getDateMonth(addedMonths));

    }

    //этотт метод для теста на отправку пустой анкеты
    public static CardInfo generateEmptyCardInfo() {
        return new CardInfo("", "", "", "", "");
    }

    @Value
    public static class CardInfo {
        String name;
        String cvv;
        String cardNumber;
        String dateYear;
        String dateMonth;
    }
}
