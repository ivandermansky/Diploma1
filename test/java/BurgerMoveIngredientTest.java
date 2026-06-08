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
public class BurgerMoveIngredientTest {

    private Burger burger;
    @Mock
    private Bun mockBun;

    private int oldIndex;
    private int newIndex;
    private String testDescription;

    // Моки ингредиентов, которые будут использоваться повторно
    private Ingredient ingredient1, ingredient2, ingredient3, ingredient4;

    public BurgerMoveIngredientTest(int oldIndex, int newIndex, String testDescription) {
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
        this.testDescription = testDescription;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        burger = new Burger();

        // Создать моки ингредиентов один раз
        ingredient1 = createIngredient("Мясо бессмертных моллюсков", 1337f, IngredientType.FILLING);
        ingredient2 = createIngredient("Соус традиционный галактический", 15f, IngredientType.SAUCE);
        ingredient3 = createIngredient("Хрустящие минеральные кольца", 300f, IngredientType.FILLING);
        ingredient4 = createIngredient("Плоды Фалленианского дерева", 874f, IngredientType.FILLING);

        // Настроить булку
        when(mockBun.getPrice()).thenReturn(100f);
        when(mockBun.getName()).thenReturn("Флюоресцентная булка R2-D3");
        burger.setBuns(mockBun);

        // Заполнить бургер (использовать 4 ингредиента для всех сценариев)
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.addIngredient(ingredient3);
        burger.addIngredient(ingredient4);
    }

    @After
    public void tearDown() {
        burger.ingredients.clear();
    }

    @Test
    public void testMoveIngredient_ShouldMoveIngredientToNewPosition() {
        System.out.println("=== Тест: " + testDescription + " ===");
        System.out.println("Перемещаем ингредиент с позиции " + oldIndex + " на позицию " + newIndex);

        // Сохранить исходный порядок для отладки
        List<Ingredient> initialOrder = new ArrayList<>(burger.ingredients);
        System.out.println("Исходный порядок: " + initialOrder);

        // Выполнить перемещение
        burger.moveIngredient(oldIndex, newIndex);

        // Формировать ожидаемый результат (копировать и менять местами)
        List<Ingredient> expectedOrder = new ArrayList<>(initialOrder);
        Ingredient movedItem = expectedOrder.remove(oldIndex);
        expectedOrder.add(newIndex, movedItem);

        System.out.println("Ожидаемый порядок: " + expectedOrder);
        System.out.println("Фактический порядок: " + burger.ingredients);

        assertThat(burger.ingredients)
                .as("После перемещения порядок ингредиентов должен совпадать с ожидаемым")
                .isEqualTo(expectedOrder);
    }

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // 1. Перемещение вверх (с конца в начало)
                {3, 0, "Перемещение последнего ингредиента (индекс 3) в начало (индекс 0)"},

                // 2. Перемещение вниз (с начала в конец)
                {0, 3, "Перемещение первого ингредиента (индекс 0) в конец (индекс 3)"},

                // 3. Перестановка соседних (обмен местами)
                {1, 2, "Перестановка соседних: ингредиент с индексом 1 на место индекса 2"},

                // 4. Перемещение в середину
                {0, 2, "Перемещение первого ингредиента (индекс 0) на середину (индекс 2)"},

                // 5. Самоперемещение (индекс не меняется)
                {2, 2, "Самоперемещение: ингредиент остается на позиции 2 (проверка граничного случая)"}
        });
    }

    // Вспомогательный метод для создания мока ингредиента
    private static Ingredient createIngredient(String name, float price, IngredientType type) {
        Ingredient ingredient = mock(Ingredient.class);
        when(ingredient.getName()).thenReturn(name);
        when(ingredient.getPrice()).thenReturn(price);
        when(ingredient.getType()).thenReturn(type);
        return ingredient;
    }
}
