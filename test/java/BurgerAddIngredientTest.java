package praktikum;

import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;

import praktikum.Burger;
import praktikum.Bun;
import praktikum.Ingredient;
import praktikum.IngredientType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerAddIngredientTest {

    private Burger burger;
    @Mock
    private Bun mockBun;

    // Параметры для текущего теста (передаются из конструктора)
    private String ingredientName;
    private float ingredientPrice;
    private String ingredientType;

    // Конструктор для параметризации
    public BurgerAddIngredientTest(String name, float price, String type) {
        this.ingredientName = name;
        this.ingredientPrice = price;
        this.ingredientType = type;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        when(mockBun.getPrice()).thenReturn(0f);
        when(mockBun.getName()).thenReturn("Test Bun");
        burger.setBuns(mockBun);
    }

    @After
    public void tearDown() {
        burger.ingredients.clear();
    }

    @Test
    public void testAddIngredientShouldAddToIngredientsAndUpdatePrice() {
        Ingredient mockIngredient = mock(Ingredient.class);
        when(mockIngredient.getName()).thenReturn(ingredientName);
        when(mockIngredient.getPrice()).thenReturn(ingredientPrice);
        when(mockIngredient.getType()).thenReturn(
                ingredientType.equals("BUN") ? IngredientType.FILLING : IngredientType.SAUCE
        );

        float priceBefore = burger.getPrice();
        burger.addIngredient(mockIngredient);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(burger.ingredients)
                .as("Список ингредиентов должен содержать добавленный ингредиент")
                .contains(mockIngredient);

        softly.assertThat(burger.getPrice())
                .as("Цена бургера должна увеличиться на стоимость добавленного ингредиента")
                .isEqualTo(priceBefore + ingredientPrice);

        softly.assertAll();
    }

    @Parameterized.Parameters(name = "Добавляем {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Булки
                {"Флюоресцентная булка R2-D3", 988f, "BUN"},
                {"Краторная булка N-200i", 1255f, "BUN"},

                // Соусы
                {"Соус Spicy-X", 90f, "SAUCE"},
                {"Соус фирменный Space Sauce", 80f, "SAUCE"},
                {"Соус традиционный галактический", 15f, "SAUCE"},
                {"Соус с шипами Антарианского плоскоходца", 88f, "SAUCE"},

                // Начинки
                {"Мясо бессмертных моллюсков Protostomia", 1337f, "FILLING"},
                {"Говяжий метеорит (отбивная)", 3000f, "FILLING"},
                {"Биокотлета из марсианской Магнолии", 424f, "FILLING"},
                {"Филе Люминесцентного тетраодонтимформа", 988f, "FILLING"},
                {"Хрустящие минеральные кольца", 300f, "FILLING"},
                {"Плоды Фалленианского дерева", 874f, "FILLING"},
                {"Кристаллы марсианских альфа-сахаридов", 762f, "FILLING"},
                {"Мини-салат Экзо-Плантаго", 4400f, "FILLING"},
                {"Сыр с астероидной плесенью", 4142f, "FILLING"}
        });
    }
}
