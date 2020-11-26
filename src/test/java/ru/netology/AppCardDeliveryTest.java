package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {
    private SelenideElement city = $("[data-test-id=city] input");
    private SelenideElement date = $("[data-test-id=date] input");
    private SelenideElement person = $("[data-test-id=name] input");
    private SelenideElement phone = $("[data-test-id=phone] input");
    private SelenideElement agreement = $("[data-test-id=agreement]");

    @Test
    void shouldConfirmRequest() {
        open("http://localhost:9999");
        city.setValue("Сыктывкар");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        LocalDate dt = LocalDate.now();
        LocalDate value = dt.plus(Period.ofDays(3));
        date.doubleClick().sendKeys(BACK_SPACE);
        date.setValue(formatter.format(value));
        person.setValue("Шамиль Газизов");
        phone.setValue("+79005553535");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

}
