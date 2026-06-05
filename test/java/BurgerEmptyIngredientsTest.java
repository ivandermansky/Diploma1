package praktikum;

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
        // Проверить цену: должно быть только удвоенная цена булочки (50 * 2 = 100)
        assertEquals(100.0f, burger.getPrice(), 0.001f);
    }

    @Test
    public void testEmptyIngredientsReceipt() {
        // Получить чек
        String receipt = burger.getReceipt();

        // Вывести чек в консоль для отладки
        System.out.println("--- Сгенерированный чек ---");
        System.out.println(receipt);
        System.out.println("------------------------");

        // Разбить чек на строки и фильтруем пустые
        String[] lines = receipt.split("\n|\r\n");
        java.util.List<String> nonEmptyLines = new java.util.ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                nonEmptyLines.add(line.trim());
            }
        }

        // Ожидать ровно 3 непустые строки
        assertEquals("Чек должен содержать ровно 3 непустые строки", 3, nonEmptyLines.size());

        // Проверить первую строку (верхняя булка)
        assertTrue("Первая строка должна содержать название булочки",
                nonEmptyLines.get(0).contains(String.format("(==== %s ====)", bun.getName())));

        // Проверить вторую строку (нижняя булка)
        assertTrue("Вторая строка должна содержать название булочки",
                nonEmptyLines.get(1).contains(String.format("(==== %s ====)", bun.getName())));

        // Проверить третью строку (цена)
        assertTrue("Третья строка должна содержать цену",
                nonEmptyLines.get(2).contains("Price:"));

        // Проверить значение цены — учитываем формат с запятой
        String priceLine = nonEmptyLines.get(2);
        assertTrue("Цена в чеке должна быть 100",
                priceLine.contains("100,") || priceLine.contains("100."));
    }
}
