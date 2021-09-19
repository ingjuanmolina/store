package com.coding.task.store.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private Order order;

    @BeforeEach
    private void init() {
        this.order = new Order();
    }

    @Test
    void anOrderWithInvalidItemThrowsIllegalArgumentException() {
        String type = "lemon";
        Entry invalidEntry = new Entry(type, 1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            order.addEntries(Collections.singletonList(invalidEntry));
        });

        String expectedMessage = "Invalid argument. " + type + " is not a valid type.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void anOrderWithOneAppleContainsOneItemAndTotalPriceIsAppleValue() {
        Entry oneApple = new Entry("apple", 1);
        order.addEntries(Collections.singletonList(oneApple));

        assertEquals(1, order.getTotalItems().size());
        assertEquals(new Apple().getPrice(), order.getTotalValue());
    }

    @Test
    void anOrderWithOneAppleAndOneOrangeContainsTwoItemsAndTotalPriceIsTheSumOfBothValues() {
        Entry oneApple = new Entry("apple", 1);
        Entry oneOrange = new Entry("orange", 1);
        order.addEntries(Arrays.asList(oneApple, oneOrange));

        assertEquals(2, order.getTotalItems().size());

        BaseGood apple = new Apple();
        BaseGood orange = new Orange();
        assertEquals(apple.getPrice().add(orange.getPrice()), order.getTotalValue());
    }

    @Test
    void orderWithTwoItemsWithQuantityGreaterThanOne() {
        int totalApples = 2;
        int totalOranges = 3;
        Entry twoApples = new Entry("apple", totalApples);
        Entry threeOranges = new Entry("orange", totalOranges);
        order.addEntries(Arrays.asList(twoApples, threeOranges));

        assertEquals(2, order.getTotalItems().size());

        BaseGood apple = new Apple();
        BaseGood orange = new Orange();
        BigDecimal totalApplesValue = apple.getPrice().multiply(BigDecimal.valueOf(totalApples));
        BigDecimal totalOrangesValue = orange.getPrice().multiply(BigDecimal.valueOf(totalOranges));
        assertEquals(totalApplesValue.add(totalOrangesValue), order.getTotalValue());
    }

    @Test
    void anOrderWithItemsWithQuantityLesserThanOneExcludesTheItem() {
        int totalApples = -1;
        int totalOranges = 3;
        Entry twoApples = new Entry("apple", totalApples);
        Entry threeOranges = new Entry("orange", totalOranges);
        order.addEntries(Arrays.asList(twoApples, threeOranges));

        assertEquals(1, order.getTotalItems().size());

        BaseGood orange = new Orange();
        BigDecimal totalOrangesValue = orange.getPrice().multiply(BigDecimal.valueOf(totalOranges));
        assertEquals(totalOrangesValue, order.getTotalValue());
    }

}