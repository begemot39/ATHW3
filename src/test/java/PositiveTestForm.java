import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class PositiveTestForm {

    public String generateDate( int days, String pattern ) {

        return LocalDate.now().plusDays( days ).format( DateTimeFormatter.ofPattern( pattern ));
    }

    @Test
    public void positiveFormTest() { // Форма заполнена корректными данными

        int daysToAdd = 3;
        String date = generateDate(daysToAdd, "dd.MM.yyyy" );

        Selenide.open("http://localhost:9999/");
        $("[data-test-id='city'] [placeholder='Город']").setValue( "Москва" );
        $("[data-test-id='date'] [placeholder='Дата встречи']").setValue( date );
        $("[data-test-id='name'] [name='name']").setValue("Иван Иванов");
        $("[data-test-id='phone'] [name='phone']").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.exactText("Забронировать")).click();

        $(Selectors.withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15));
    }

}
