package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class AppCardDeliveryTest {
    private SelenideElement cityField = $("[data-test-id=city] input");
    private SelenideElement date = $("[data-test-id=date] input");
    private SelenideElement personName = $("[data-test-id=name] input");
    private SelenideElement phoneNumber = $("[data-test-id=phone] input");
    private SelenideElement agreement = $("[data-test-id=agreement]");
    private SelenideElement bookingButton = $$("button").find(exactText("Забронировать"));
    private SelenideElement successMessage = $(withText("Успешно!"));
    private SelenideElement rejectPhoneNumberMessage = $(byText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    private SelenideElement rejectCityValueMessage = $(byText("Доставка в выбранный город недоступна"));

    @BeforeEach
    void shouldOpenBrowser() { open("http://localhost:9999"); }

    @Test
    void shouldConfirmRequestSkipCalendar() {
        cityField.setValue("Сыктывкар");
        personName.setValue("Шамиль Газизов");
        phoneNumber.setValue("+79005553535");
        agreement.click();
        bookingButton.click();
        successMessage.waitUntil(visible, 15000);
        //$("[data-test-id=notification] .notification__content").shouldHave(text("Встреча успешно забронирована на "));
        //$(withText("Успешно! Встреча успешно забронирована на " + date.getValue())).waitUntil(visible, 15000);
        //$(withText("Успешно! " + "Встреча успешно забронирована на ")).waitUntil(visible, 15000);
    }

    @Test
    void shouldConfirmRequestUpToSevenDays() {
        cityField.setValue("Сы");
        $(withText("Сыктывкар")).click();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        LocalDate local = LocalDate.now();
        LocalDate value = local.plus(Period.ofDays(7));
        date.doubleClick().sendKeys(BACK_SPACE);
        date.setValue(formatter.format(value));
        personName.setValue("Шамиль Газизов");
        phoneNumber.setValue("+79005553535");
        agreement.click();
        bookingButton.click();
        successMessage.waitUntil(visible, 15000);
    }

    @Test
    void shouldNotConfirmRequestIncorrectNumber() {
        cityField.setValue("Уфа");
        personName.setValue("Шамиль Газизов");
        phoneNumber.setValue("7-900-555-35-35");
        agreement.click();
        bookingButton.click();
        rejectPhoneNumberMessage.waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestIncorrectCityLatinLetters() {
        cityField.setValue("Helsinki");
        personName.setValue("Шамиль Газизов");
        phoneNumber.setValue("+79005553535");
        agreement.click();
        bookingButton.click();
        rejectCityValueMessage.waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestIncorrectCity() {
        cityField.setValue("Хельсинки");
        personName.setValue("Шамиль Газизов");
        phoneNumber.setValue("+79005553535");
        agreement.click();
        bookingButton.click();
        rejectCityValueMessage.waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestEmptyFieldName() {
        cityField.setValue("Сыктывкар");
        phoneNumber.setValue("+79005553535");
        agreement.click();
        bookingButton.click();
        $(withText("Поле обязательно для заполнения")).waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestIncorrectName() {
        cityField.setValue("Сыктывкар");
        personName.setValue("Shamil Gazizov");
        phoneNumber.setValue("+79005553535");
        agreement.click();
        bookingButton.click();
        $(withText("Имя и Фамилия указаные неверно")).waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestIfNotSetCheckbox() {
        cityField.setValue("Сыктывкар");
        personName.setValue("Шамиль Газизов");
        phoneNumber.setValue("+79005553535");
        bookingButton.click();
        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
