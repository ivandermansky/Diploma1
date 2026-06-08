package praktikum;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerGetPriceTest {

    private Burger burger;
    @Mock
    private Bun mockBun;

    // Параметры для текущего теста
    private float bunPrice;
    private List<Ingredient> testIngredients;
    private float expectedTotalPrice;

    // Конструктор для параметризации
    public BurgerGetPriceTest(float bunPrice, List<Ingredient> ingredients, float expectedPrice) {
        this.bunPrice = bunPrice;
        this.testIngredients = ingredients;
        this.expectedTotalPrice = expectedPrice;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        // Настроить мок булочки
        when(mockBun.getPrice()).thenReturn(bunPrice);
        when(mockBun.getName()).thenReturn("Test Bun");
        burger.setBuns(mockBun);

        // Добавить ингредиенты из тестового набора
        for (Ingredient ingredient : testIngredients) {
            burger.addIngredient(ingredient);
        }
    }

    @After
    public void tearDown() {
        burger.ingredients.clear();
    }

    @Test
    public void testGetPrice_ShouldCalculateTotalCorrectly() {
        float actualPrice = burger.getPrice();

        assertThat(actualPrice)
                .as("Итоговая цена бургера должна равняться (цена булочки * 2) + сумма цен ингредиентов")
                .isEqualTo(expectedTotalPrice);
    }

    // Метод, возвращающий коллекцию параметров
    @Parameterized.Parameters(name = "Булка {0} + {1} ингредиентов = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{

                // Сложный состав
                {30f, Arrays.asList(
                        createIngredient("Соус Spicy-X", 90f, IngredientType.SAUCE),
                        createIngredient("Мясо моллюсков", 1337f, IngredientType.FILLING),
                        createIngredient("Сыр с плесенью", 4142f, IngredientType.FILLING)
                ), 5629f} // Исправленное значение: 30*2 + 90 + 1337 + 4142 = 5629
        });
    }

    // Вспомогательный метод для создания мока ингредиента
    public static Ingredient createIngredient(String name, float price, IngredientType type) {
        Ingredient ingredient = mock(Ingredient.class);
        when(ingredient.getName()).thenReturn(name);
        when(ingredient.getPrice()).thenReturn(price);
        when(ingredient.getType()).thenReturn(type);
        return ingredient;
    }
}
