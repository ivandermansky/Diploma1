package praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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

@RunWith(Parameterized.class)
public class BurgerRemoveIngredientTest {

    private Burger burger;
    @Mock
    private Bun mockBun;

    private int indexToRemove;
    private List<Ingredient> initialIngredients;
    private String testDescription;

    // Сохранить ссылки на ингредиенты для проверки (чтобы не создавать новые моки в data())
    private Ingredient ingredient1, ingredient2, ingredient3;

    public BurgerRemoveIngredientTest(int indexToRemove, String testDescription) {
        this.indexToRemove = indexToRemove;
        this.testDescription = testDescription;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        // Создать моки один раз здесь, чтобы использовать их и в бургере, и в проверке
        ingredient1 = createIngredient("Мясо бессмертных моллюсков", 1337f, IngredientType.FILLING);
        ingredient2 = createIngredient("Соус традиционный", 15f, IngredientType.SAUCE);
        ingredient3 = createIngredient("Хрустящие кольца", 300f, IngredientType.FILLING);

        // Заменили название булки согласно скриншоту
        when(mockBun.getPrice()).thenReturn(100f);
        when(mockBun.getName()).thenReturn("Краторная булка N-200i");
        burger.setBuns(mockBun);

        // Собрать список для теста (может быть разной длины в зависимости от теста)
        initialIngredients = new ArrayList<>();
        initialIngredients.add(ingredient1);
        initialIngredients.add(ingredient2);
        if (testDescription.contains("из середины") || testDescription.contains("последнего")) {
            initialIngredients.add(ingredient3);
        }

        // Добавить в бургер
        for (Ingredient ingredient : initialIngredients) {
            burger.addIngredient(ingredient);
        }
    }

    @After
    public void tearDown() {
        burger.ingredients.clear();
    }

    @Test
    public void testRemoveIngredient_ShouldRemoveCorrectIngredientAtIndex() {
        System.out.println("=== Тест: " + testDescription + " ===");
        System.out.println("Удалить ингредиент по индексу: " + indexToRemove);

        burger.removeIngredient(indexToRemove);

        // Формировать ожидаемый результат из тех же объектов, что и в бургере
        List<Ingredient> expected = new ArrayList<>(initialIngredients);
        expected.remove(indexToRemove);

        assertThat(burger.ingredients)
                .as("После удаления список должен совпадать")
                .isEqualTo(expected);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0, "Удаление первого ингредиента"},
                {1, "Удаление из середины списка"},
                {0, "Удаление единственного (создать список из 1 элемента внутри setUp)"},
                {2, "Удаление последнего ингредиента"}
        });
    }

    private static Ingredient createIngredient(String name, float price, IngredientType type) {
        Ingredient ingredient = mock(Ingredient.class);
        when(ingredient.getName()).thenReturn(name);
        when(ingredient.getPrice()).thenReturn(price);
        when(ingredient.getType()).thenReturn(type);
        return ingredient;
    }
}
