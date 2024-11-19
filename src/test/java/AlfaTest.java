import Data.DataBaseHelper;
import Data.DataHelper;

import PajeObjects.FormPage;
import PajeObjects.MainPage;
import PajeObjects.NotificationPage;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static Data.DataBaseHelper.cleanDataBase;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlfaTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void tearDown() {
        cleanDataBase();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void starting() {
        open("http://localhost:8080");
    }

    @Test
    void successBuyForm() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo validCard = DataHelper.generateCard(1, 0, true);
        formPage.fillForm(validCard);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.waitForMessage();

        String actualSuccessMessage = notificationPage.getSuccessMessage();
        String expectedSuccessMessage = "Операция одобрена Банком";


        assertTrue(actualSuccessMessage.contains(expectedSuccessMessage), expectedSuccessMessage);
    }

    @Test
    void successCreditBuyForm() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyOnCreditButton();
        FormPage formPage = mainPage.checkCreditFormVisible();
        DataHelper.CardInfo validCard = DataHelper.generateCard(1, 0, true);
        formPage.fillForm(validCard);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.waitForMessage();

        String actualSuccessMessage = notificationPage.getSuccessMessage();
        String expectedSuccessMessage = "Операция одобрена Банком";


        assertTrue(actualSuccessMessage.contains(expectedSuccessMessage), expectedSuccessMessage);
    }

    @Test
    void errorBuyForm() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo validCard = DataHelper.generateCard(1, 0, false);
        formPage.fillForm(validCard);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.waitForMessage();

        String actualErrorMessage = notificationPage.getErrorMessage();
        String expectedErrorMessage = "Банк отказал в проведении операции";


        assertTrue(actualErrorMessage.contains(expectedErrorMessage), expectedErrorMessage);
    }

    @Test
    void errorCreditBuyForm() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyOnCreditButton();
        FormPage formPage = mainPage.checkCreditFormVisible();
        DataHelper.CardInfo validCard = DataHelper.generateCard(1, 0, false);
        formPage.fillForm(validCard);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.waitForMessage();

        String actualErrorMessage = notificationPage.getErrorMessage();
        String expectedErrorMessage = "Банк отказал в проведении операции";


        assertTrue(actualErrorMessage.contains(expectedErrorMessage), expectedErrorMessage);
    }

    @Test
    void blankForm() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo blankForm = DataHelper.generateEmptyCardInfo();
        formPage.fillForm(blankForm);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.checkFieldError("card number", "Поле обязательно для заполнения");
        formPage.checkFieldError("month", "Поле обязательно для заполнения");
        formPage.checkFieldError("year", "Поле обязательно для заполнения");
        formPage.checkFieldError("owner", "Поле обязательно для заполнения");
        formPage.checkFieldError("cvv", "Поле обязательно для заполнения");
    }

    @Test
    void oldCardYear() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo oldYear = DataHelper.generateCard(-1, 0, true);
        formPage.fillForm(oldYear);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.checkFieldError("year", "Истёк срок действия карты");
    }

    @Test
    void oldMonth() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo oldYear = DataHelper.generateCard(0, -1, true);
        formPage.fillForm(oldYear);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.checkFieldError("month", "Истёк срок действия карты");

    }

    @Test
    void wrongMonth() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo oldYear = DataHelper.generateCard(0, 10, true);
        formPage.fillForm(oldYear);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.checkFieldError("month", "Неверно указан срок действия карты");
    }

    @Test
    void invalidName() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo invalidName = new DataHelper
                .CardInfo(
                "123",
                "999",
                "4444444444444441",
                "27",
                "12"
        );
        formPage.fillForm(invalidName);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.checkFieldError("owner", "Неверный формат");
    }

    @Test
    void invalidCvv() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo invalidCvv = new DataHelper
                .CardInfo(
                "aaaa",
                "99",
                "4444444444444441",
                "27",
                "12"
        );
        formPage.fillForm(invalidCvv);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.checkFieldError("cvv", "Неверный формат");
    }

    @Test
    void creditDeclinedDb() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyOnCreditButton();
        FormPage formPage = mainPage.checkCreditFormVisible();
        DataHelper.CardInfo validCard = DataHelper.generateCard(1, 0, false);
        formPage.fillForm(validCard);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.waitForMessage();
        var actualStatus = DataBaseHelper.getCreditStatus();
        String expectedStatus = "DECLINED";


        assertTrue(actualStatus.contains(expectedStatus), expectedStatus);
    }

    @Test
    void buyDeclinedDb() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo validCard = DataHelper.generateCard(1, 0, false);
        formPage.fillForm(validCard);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.waitForMessage();
        var actualStatus = DataBaseHelper.getBuyStatus();
        String expectedStatus = "DECLINED";


        assertTrue(actualStatus.contains(expectedStatus), expectedStatus);
    }

    @Test
    void creditApprovedDb() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyOnCreditButton();
        FormPage formPage = mainPage.checkCreditFormVisible();
        DataHelper.CardInfo validCard = DataHelper.generateCard(1, 0, true);
        formPage.fillForm(validCard);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.waitForMessage();
        var actualStatus = DataBaseHelper.getCreditStatus();
        String expectedStatus = "APPROVED";


        assertTrue(actualStatus.contains(expectedStatus), expectedStatus);
    }

    @Test
    void buyApprovedDb() {
        MainPage mainPage = new MainPage();
        mainPage.clickBuyButton();
        FormPage formPage = mainPage.checkPaymentFormVisible();
        DataHelper.CardInfo validCard = DataHelper.generateCard(1, 0, true);
        formPage.fillForm(validCard);
        NotificationPage notificationPage = formPage.submitForm();
        formPage.waitForMessage();
        var actualStatus = DataBaseHelper.getBuyStatus();
        String expectedStatus = "APPROVED";


        assertTrue(actualStatus.contains(expectedStatus), expectedStatus);
    }

}