package praktikum;

import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import praktikum.IngredientType;
import praktikum.Ingredient;
import praktikum.Burger;
import praktikum.Bun;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerSetBunsTest {

    private Burger burger;
    @Mock
    private Bun mockBun;

    private String bunName;
    private float bunPrice;
    private String testDescription;

    public BurgerSetBunsTest(String bunName, float bunPrice, String testDescription) {
        this.bunName = bunName;
        this.bunPrice = bunPrice;
        this.testDescription = testDescription;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        // Настроить мок булочки с данными с картинки
        when(mockBun.getName()).thenReturn(bunName);
        when(mockBun.getPrice()).thenReturn(bunPrice);
    }

    @After
    public void tearDown() {
        // Очистка не требуется, так как мы тестируем только установку
    }

    @Test
    public void testSetBunsShouldSetBunCorrectly() {
        System.out.println("=== Тест: " + testDescription + " ===");
        System.out.println("Устанавливаем булку: " + bunName + " (цена: " + bunPrice + ")");


        // Выполнить установку
        burger.setBuns(mockBun);

        // Создать экземпляр SoftAssertions
        SoftAssertions softly = new SoftAssertions();


        // Все проверки внутри блока SoftAssertions
        softly.assertThat(burger.bun)
                .as("Поле bun в бургере должно ссылаться на переданную булку")
                .isEqualTo(mockBun);

        softly.assertThat(burger.bun.getName())
                .as("Имя булки должно совпадать")
                .isEqualTo(bunName);

        softly.assertThat(burger.bun.getPrice())
                .as("Цена булки должна совпадать")
                .isEqualTo(bunPrice);

        // Обязательный вызов — собирает все ошибки и выбрасывает единую AssertionError, если есть сбои
        softly.assertAll();
    }

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Флюоресцентная булка R2-D3", 988f, "Установка флюоресцентной булки R2-D3 (цена 988)"},
                {"Краторная булка N-200i", 1255f, "Установка краторной булки N-200i (цена 1255)"}
        });
    }
}
