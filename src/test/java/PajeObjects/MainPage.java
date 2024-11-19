package PajeObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private SelenideElement buyButton =
            $("button.button_size_m.button_theme_alfa-on-white")
                    .find("span.button__text");  // Кнопка "Купить"

    private SelenideElement buyOnCreditButton =
            $("button.button_view_extra.button_size_m.button_theme_alfa-on-white")
                    .find("span.button__text");  // Кнопка "Купить в кредит"

    // Уточненный локатор для заголовка "Оплата по карте"
    private SelenideElement paymentFormTitle =
            $x("//h3[contains(text(), 'Оплата по карте') or contains(text(), 'Кредит по данным карты')]");

    // Метод для нажатия кнопки "Купить"
    public void clickBuyButton() {
        buyButton.shouldBe(Condition.visible).click();
    }

    public void clickBuyOnCreditButton() {
        buyOnCreditButton.shouldBe(Condition.visible).click();
    }

    // Проверка видимости формы "Оплата по карте"
    public FormPage checkPaymentFormVisible() {
        paymentFormTitle.shouldBe(Condition.visible)
                .shouldHave(Condition.text("Оплата по карте"));
        return new FormPage();
    }

    // Проверка видимости формы "Кредит по данным карты"
    public FormPage checkCreditFormVisible() {
        paymentFormTitle.shouldBe(Condition.visible)
                .shouldHave(Condition.text("Кредит по данным карты"));
        return new FormPage();
    }
}