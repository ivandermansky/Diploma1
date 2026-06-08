package praktikum;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import praktikum.Bun;
import praktikum.Burger;

/*
 - Тест для проверки поведения бургера при пустом списке ингредиентов.
 */

public class BurgerEmptyIngredientsTest {

    private Burger burger;
    private Bun bun;

    @Before
    public void setUp() {
        // Создать булочку для теста
        bun = new Bun("test bun", 50);
        // Создать пустой бургер
        burger = new Burger();
        burger.setBuns(bun); // Установить булочку
    }

    @Test
    public void testEmptyIngredientsPrice() {
        SoftAssertions softly = new SoftAssertions();

        // Проверить цену: должна быть только удвоенная цена булочки (50 * 2 = 100)
        softly.assertThat(burger.getPrice())
                .as("Цена должна быть удвоенной ценой булочки (50 * 2 = 100)")
                .isEqualTo(100.0f);

        softly.assertAll();
    }

    @Test
    public void testEmptyIngredientsReceipt() {
        // Получить чек
        String receipt = burger.getReceipt();

        // Вывести чек в консоль для отладки
        System.out.println("--- Сгенерированный чек ---");
        System.out.println(receipt);
        System.out.println("------------------------");

        // Разбить чек на строки и фильтровать пустые
        String[] lines = receipt.split("\n|\r\n");
        java.util.List<String> nonEmptyLines = new java.util.ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                nonEmptyLines.add(line.trim());
            }
        }

        SoftAssertions softly = new SoftAssertions();

        // Ожидать ровно 3 непустые строки
        softly.assertThat(nonEmptyLines.size())
                .as("Чек должен содержать ровно 3 непустые строки")
                .isEqualTo(3);

        // Проверить первую строку (верхняя булка)
        softly.assertThat(nonEmptyLines.get(0))
                .as("Первая строка должна содержать название булочки")
                .contains(String.format("(==== %s ====)", bun.getName()));

        // Проверить вторую строку (нижняя булка)
        softly.assertThat(nonEmptyLines.get(1))
                .as("Вторая строка должна содержать название булочки")
                .contains(String.format("(==== %s ====)", bun.getName()));

        // Проверить третью строку (цена)
        softly.assertThat(nonEmptyLines.get(2))
                .as("Третья строка должна содержать цену")
                .contains("Price:");

        // Проверить значение цены — учесть формат с запятой
        String priceLine = nonEmptyLines.get(2);
        softly.assertThat(priceLine)
                .as("Цена в чеке должна быть 100")
                .matches(".*(100,|100.).*");

        softly.assertAll();
    }
}
