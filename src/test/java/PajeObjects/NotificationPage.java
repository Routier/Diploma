package PajeObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class NotificationPage {
    private SelenideElement successNotification = $(".notification_status_ok");
    private SelenideElement errorNotification = $(".notification_status_error");

    public SelenideElement getSuccessNotification() {
        return successNotification.shouldBe(Condition.visible);
    }
    public SelenideElement getErrorNotification() {
        return errorNotification.shouldBe(Condition.visible);
    }

    public String getSuccessMessage() {
        return successNotification.$(".notification__content").getText();
    }

    public String getErrorMessage() {
        return errorNotification.$(".notification__content").getText();
    }
}
