package praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import praktikum.IngredientType;
import praktikum.Ingredient;
import praktikum.Burger;
import praktikum.Bun;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static praktikum.BurgerGetPriceTest.createIngredient;

@RunWith(Parameterized.class)
public class BurgerGetReceiptTest {

    private Burger burger;
    @Mock
    private Bun mockBun;

    // Параметры для текущего теста
    private String bunName;
    private List<Ingredient> testIngredients;
    private float expectedPrice;
    private String expectedReceipt;

    // Конструктор для параметризации
    public BurgerGetReceiptTest(String bunName, List<Ingredient> ingredients,
                                float expectedPrice, String expectedReceipt) {
        this.bunName = bunName;
        this.testIngredients = ingredients;
        this.expectedPrice = expectedPrice;
        this.expectedReceipt = expectedReceipt;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        // Настроить мок булочки
        when(mockBun.getPrice()).thenReturn(100f);
        when(mockBun.getName()).thenReturn(bunName);
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
    public void testGetReceipt_ShouldGenerateCorrectReceiptFormat() {
        String actualReceipt = burger.getReceipt();

        // Нормализовать обе строки
        String normalizedActual = normalizeReceipt(actualReceipt);
        String normalizedExpected = normalizeReceipt(expectedReceipt);

        System.out.println("=== Ожидаемый чек (нормализованный) ===");
        System.out.println(normalizedExpected);
        System.out.println("=== Фактический чек (нормализованный) ===");
        System.out.println(normalizedActual);
        System.out.println("====================");

        assertThat(normalizedActual)
                .as("Чек должен соответствовать ожидаемому формату и содержимому")
                .isEqualTo(normalizedExpected);
    }

    /*
     - Вспомогательный метод для нормализации чека:
     - заменяет запятые на точки в ценах
     - нормализует переносы строк
     - убирает пробелы в начале/конце строк
     - оставляет 1 знак после точки в цене
     - удаляет пустые строки в конце
     */
    private String normalizeReceipt(String receipt) {
        // 1. Заменить запятые на точки в части с ценой
        String result = receipt.replace(',', '.');

        // 2. Нормализовать переносы строк: \r\n и \r → \n
        result = result.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");

        // 3. Разбить на строки и убираем пробелы в начале и конце каждой
        String[] lines = result.split("\n");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
        }

        // 4. Собрать строки обратно с \n
        result = String.join("\n", lines);

        // 5. Оставить только один знак после точки в цене (и удалить лишние нули)
        result = result.replaceAll("(\\.\\d)\\d*", "\\$1");

        // 6. Убрать пустые строки в конце
        result = result.replaceAll("\n+\\$", "");

        return result;
    }

    // Метод, возвращающий коллекцию параметров
    @Parameterized.Parameters(name = "Чек для булки '{0}' с {1} ингредиентами")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{

                // Случай 1: Бургер без ингредиентов
                {
                        "Флюоресцентная булка R2-D3",
                        Arrays.asList(),
                        200f, // 100 * 2
                        "(==== Флюоресцентная булка R2-D3 ====)\n" +
                                "(==== Флюоресцентная булка R2-D3 ====)\n" +
                                "\nPrice: 200.0\n"
                },

                // Случай 2: Булка + один соус
                {
                        "Краторная булка N-200i",
                        Arrays.asList(
                                createIngredient("Соус Spicy-X", 90f, IngredientType.SAUCE)
                        ),
                        290f, // 100 * 2 + 90
                        "(==== Краторная булка N-200i ====)\n" +
                                "= sauce Соус Spicy-X =\n" +
                                "(==== Краторная булка N-200i ====)\n" +
                                "\nPrice: 290.0\n"
                },

                // Случай 3: Булка + два ингредиента (соус и начинка)
                {
                        "Флюоресцентная булка R2-D3",
                        Arrays.asList(
                                createIngredient("Мясо бессмертных моллюсков Protostomia", 1337f, IngredientType.FILLING),
                                createIngredient("Соус фирменный Space Sauce", 80f, IngredientType.SAUCE)
                        ),
                        1617f, // 100 * 2 + 1337 + 80 = 1617
                        "(==== Флюоресцентная булка R2-D3 ====)\n" +
                                "= filling Мясо бессмертных моллюсков Protostomia =\n" +
                                "= sauce Соус фирменный Space Sauce =\n" +
                                "(==== Флюоресцентная булка R2-D3 ====)\n" +
                                "\nPrice: 1617.0\n"  // ← исправлено здесь
                },

                // Случай 4: Булка с несколькими ингредиентами
                {
                        "Краторная булка N-200i",
                        Arrays.asList(
                                createIngredient("Мясо бессмертных моллюсков Protostomia", 1337f, IngredientType.FILLING),
                                createIngredient("Хрустящие минеральные кольца", 300f, IngredientType.FILLING),
                                createIngredient("Соус традиционный галактический", 15f, IngredientType.SAUCE)
                        ),
                        1852f, // 100*2 + 1337 + 300 + 15 = 1852
                        "(==== Краторная булка N-200i ====)\n" +
                                "= filling Мясо бессмертных моллюсков Protostomia =\n" +
                                "= filling Хрустящие минеральные кольца =\n" +
                                "= sauce Соус традиционный галактический =\n" +
                                "(==== Краторная булка N-200i ====)\n" +
                                "\nPrice: 1852.0\n"
                }
        });
    }
}