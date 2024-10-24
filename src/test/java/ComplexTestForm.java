import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDate;


import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ComplexTestForm {

    public int getSumOfDays( int days  ) { // Вернуть сумму текущей даты и дней от пользователя.
        LocalDate currentDate = LocalDate.now();
        int day = currentDate.getDayOfMonth();
        return day + days;
    }

    public String getDateDay( int days ) { // Вернуть только день из даты назначенной пользователем.

        LocalDate currentDate = LocalDate.now();
        return String.valueOf(currentDate.plusDays( days ).getDayOfMonth());
    }

    @Test
    public void testComplexElementForm() {

        String testLetters = "Ка"; // Переменная для первых букв города.
        String testCity = "Казань"; // Значение города, которое нужно найти.
        int days = 7; // Количество дней, через котрое требуется доставить карту.
        int sumOfDays = getSumOfDays(days); // Сумма текущего дня и дней для доставки карты.

        Selenide.open("http://localhost:9999/");
        $("[data-test-id='city'] [placeholder='Город']").setValue( testLetters ); // Ввести в поле города первые две буквы.
        $$(".menu-item__control").find(Condition.exactText(testCity)).click(); // Выбрать город из выпадающего списка.
        $("[data-test-id='date'] [placeholder='Дата встречи']").click(); // Нажать на поле с датой.
        SelenideElement numOfMaxDays = $$(".calendar__day").filter(Condition.attribute("data-day")).last(); // Получить последний элемент из коллекции дней месяца.
        String dayToString =  numOfMaxDays.getText(); // Преобразовать числовое значение последнего элемента из коллекции выше в строку.
        int stringToNumber = Integer.parseInt(dayToString); // Преобразовать строку последнего элемента в число.
        if ( sumOfDays > stringToNumber ) { // Сравнивает сумму дней и максимальное количество дней в месяце
            $("[data-step='1']").click(); // Если сумма дней больше количества дней в месяце, переворачиваем страницу календаря и выбираем элемент с датой в следующем месяце.
            $(Selectors.byText(getDateDay(  days ))).click();
        } else { // Если сумма дней текущей даты и количество дней доставки меньше дней месяца, выбираем её в теущем месяце.
            $(Selectors.byText(getDateDay(  days ))).click();
        }

        $("[data-test-id='name'] [name='name']").setValue("Иван Иванов");
        $("[data-test-id='phone'] [name='phone']").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.exactText("Забронировать")).click();

        $(Selectors.withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(Selectors.withText("Встреча успешно забронирована на")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(Selectors.withText( getDateDay(  days ) )).shouldBe(Condition.visible, Duration.ofSeconds(15));

    }
}
