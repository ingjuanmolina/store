package com.coding.task.store.service;

import com.coding.task.store.model.Apple;
import com.coding.task.store.model.BaseGood;
import com.coding.task.store.model.Entry;
import com.coding.task.store.model.Orange;
import com.coding.task.store.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceTest {
    private OrderService service;

    @BeforeEach
    private void init() {
        this.service = new OrderService();
    }

    @Test
    void anOrderWithInvalidItemThrowsIllegalArgumentException() {
        String type = "lemon";
        Entry invalidEntry = new Entry(type, 1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getOrder(Collections.singletonList(invalidEntry));
        });

        String expectedMessage = "Invalid argument. " + type + " is not a valid type.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void anOrderWithOneAppleContainsOneItemAndTotalPriceIsAppleValue() {
        Entry oneApple = new Entry("apple", 1);
        Order order = service.getOrder(Collections.singletonList(oneApple));

        assertEquals(1, order.getItems().size());
        assertEquals(new Apple().getPrice(), order.getTotalValue());
    }

    @Test
    void anOrderWithOneAppleAndOneOrangeContainsTwoItemsAndTotalPriceIsTheSumOfBothValues() {
        Entry oneApple = new Entry("apple", 1);
        Entry oneOrange = new Entry("orange", 1);
        Order order = service.getOrder(Arrays.asList(oneApple, oneOrange));

        assertEquals(2, order.getItems().size());

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
        Order order = service.getOrder(Arrays.asList(twoApples, threeOranges));

        assertEquals(2, order.getItems().size());

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
        Order order = service.getOrder(Arrays.asList(twoApples, threeOranges));

        assertEquals(1, order.getItems().size());

        BaseGood orange = new Orange();
        BigDecimal totalOrangesValue = orange.getPrice().multiply(BigDecimal.valueOf(totalOranges));
        assertEquals(totalOrangesValue, order.getTotalValue());
    }

    @Test
    void anOrderWithoutItemThrowsIllegalStateException() {
        String type = "orange";
        Entry onePositiveEntry = new Entry(type, 1);
        Entry oneNegativeEntry = new Entry(type, -1);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            service.getOrder(Arrays.asList(onePositiveEntry, oneNegativeEntry));
        });

        String expectedMessage = "Order doesn't contain any items.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void orderWithTwoItemsWithQuantityGreaterThanOneApplyingOffer() {
        int totalApples = 15;
        int totalOranges = 20;
        Entry twoApples = new Entry("apple", totalApples);
        Entry threeOranges = new Entry("orange", totalOranges);
        Order order = service.getOrder(Arrays.asList(twoApples, threeOranges));

        assertEquals(2, order.getItems().size());

        BaseGood apple = new Apple();
        BaseGood orange = new Orange();

        int appleQuantityToGetPrice = (totalApples / 3) * 2 + totalApples % 3;
        int orangeQuantityToGetPrice = totalOranges - totalOranges / 2;

        BigDecimal totalApplesValue = apple.getPrice().multiply(BigDecimal.valueOf(totalApples));
        BigDecimal totalOrangesValue = orange.getPrice().multiply(BigDecimal.valueOf(totalOranges));
        BigDecimal totalValue = totalApplesValue.add(totalOrangesValue);

        assertEquals(totalValue, order.getTotalValue());

        BigDecimal totalApplesValueWithDiscount = apple.getPrice().multiply(BigDecimal.valueOf(appleQuantityToGetPrice));
        BigDecimal totalOrangesValueWithDiscount = orange.getPrice().multiply(BigDecimal.valueOf(orangeQuantityToGetPrice));
        BigDecimal totalValueWithDiscount = totalApplesValueWithDiscount.add(totalOrangesValueWithDiscount);

        assertEquals(totalValueWithDiscount, order.getTotalValueAfterDiscount());

        assertEquals(totalValue.subtract(totalValueWithDiscount), order.getDiscount());
    }

}
