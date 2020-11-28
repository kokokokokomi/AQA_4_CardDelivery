package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {
    private SelenideElement city = $("[data-test-id=city] input");
    private SelenideElement date = $("[data-test-id=date] input");
    private SelenideElement name = $("[data-test-id=name] input");
    private SelenideElement phone = $("[data-test-id=phone] input");
    private SelenideElement agreement = $("[data-test-id=agreement]");

    @BeforeEach
    void shouldOpenBrowser() { open("http://localhost:9999"); }

    @Test
    void shoulConfirmRequestSkipCalendar() {
        city.setValue("Сыктывкар");
        name.setValue("Шамиль Газизов");
        phone.setValue("+79005553535");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

    @Test
    void shouldConfirmRequestUpToSevenDays() {
        city.setValue("Сыктывкар");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        LocalDate local = LocalDate.now();
        //LocalDate value = local.plusDays(7);
        LocalDate value = local.plus(Period.ofDays(7));
        date.setValue(formatter.format(value));
        name.setValue("Шамиль Газизов");
        phone.setValue("+79005553535");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

    @Test
    void shouldNotConfirmRequestIncorrectNumber() {
        city.setValue("Уфа");
        name.setValue("Шамиль Газизов");
        phone.setValue("7-900-555-35-35");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Телефон указан неверно.")).waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestIncorrectCityLatinLetters() {
        city.setValue("Helsinki");
        name.setValue("Шамиль Газизов");
        phone.setValue("+79005553535");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestIncorrectCity() {
        city.setValue("Хельсинки");
        name.setValue("Шамиль Газизов");
        phone.setValue("+79005553535");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestEmptyFieldName() {
        city.setValue("Сыктывкар");
        phone.setValue("+79005553535");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestIncorrectName() {
        city.setValue("Сыктывкар");
        name.setValue("Shamil Gazizov");
        phone.setValue("+79005553535");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно")).waitUntil(visible, 5000);
    }

    @Test
    void shouldNotConfirmRequestIfNotSetCheckbox() {
        city.setValue("Сыктывкар");
        name.setValue("Шамиль Газизов");
        phone.setValue("+79005553535");
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldConfirmRequestWithTwoLetters() {
        city.setValue("Сы");
        $(withText("Сыктывкар")).click();
        name.setValue("Шамиль Газизов");
        phone.setValue("+79005553535");
        agreement.click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }
}
