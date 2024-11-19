package PajeObjects;

import Data.DataHelper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class FormPage {

    // Карта для сопоставления полей и их меток
    private Map<String, String> fieldToLabelMap = Map.of(
            "card number", "Номер карты",
            "month", "Месяц",
            "year", "Год",
            "owner", "Владелец",
            "cvv", "CVC/CVV");

    // Локаторы для ошибок на форме
    private SelenideElement cardNumberError =
            $x("//span[@class='input__top' and text()='Номер карты']/following-sibling::span[@class='input__sub']");
    private SelenideElement monthError =
            $x("//span[@class='input__top' and text()='Месяц']/following-sibling::span[@class='input__sub']");
    private SelenideElement yearError =
            $x("//span[@class='input__top' and text()='Год']/following-sibling::span[@class='input__sub']");
    private SelenideElement ownerError =
            $x("//span[@class='input__top' and text()='Владелец']/following-sibling::span[@class='input__sub']");
    private SelenideElement cvvError =
            $x("//span[@class='input__top' and text()='CVC/CVV']/following-sibling::span[@class='input__sub']");

    // Локаторы для полей ввода формы
    private SelenideElement cardNumberInput =
            $("input.input__control[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthInput =
            $x("//span[@class='input__top' and text()='Месяц']/following-sibling::span//input");
    private SelenideElement yearInput =
            $x("//span[@class='input__top' and text()='Год']/following-sibling::span//input");
    private SelenideElement ownerInput =
            $x("//span[@class='input__top' and contains(text(),'Владелец')]/following-sibling::span//input");
    private SelenideElement cvvInput =
            $("input.input__control[placeholder='999']");

    // Кнопка отправки формы
    private SelenideElement continueButton = $$("button")
            .filterBy(Condition.text("Продолжить"))
            .first();

    private SelenideElement notificationMessage = $(".notification.notification_status_ok .notification__title");

    // Метод для заполнения полей формы, с возможностью оставлять поля пустыми.
    private void setInputValue(SelenideElement element, String value) {
        if (value != null && !value.isEmpty()) {
            element.setValue(value);
        }
    }

    // Заполнение формы
    public FormPage fillForm(DataHelper.CardInfo cardInfo) {
        setInputValue(cardNumberInput, cardInfo.getCardNumber());
        setInputValue(monthInput, cardInfo.getDateMonth());
        setInputValue(yearInput, cardInfo.getDateYear());
        setInputValue(ownerInput, cardInfo.getName());
        setInputValue(cvvInput, cardInfo.getCvv());

        return this;
    }

    // Получение локатора ошибки поля (невалидные данные или пустое поле)
    private SelenideElement getFieldError(String fieldName) {
        String fieldLabel = fieldToLabelMap.get(fieldName.toLowerCase());

        switch (fieldLabel) {
            case "Номер карты":
                return cardNumberError;
            case "Месяц":
                return monthError;
            case "Год":
                return yearError;
            case "Владелец":
                return ownerError;
            case "CVC/CVV":
                return cvvError;
            default:
                throw new IllegalArgumentException("Поле  '" + fieldName + "' не найдено");
        }
    }

    // Проверка ошибки для конкретного поля
    public FormPage checkFieldError(String fieldName, String expectedError) {
        SelenideElement errorElement = getFieldError(fieldName);
        errorElement.shouldBe(Condition.visible).shouldHave(Condition.text(expectedError));
        return this;
    }

    // Нажатие кнопки "Продолжить"
    public NotificationPage submitForm() {
        continueButton.shouldBe(Condition.visible).click();
        return new NotificationPage();
    }

    public FormPage waitForMessage() {
        notificationMessage.shouldBe(Condition.visible, Duration.ofSeconds(15));
        return this;
    }
}
