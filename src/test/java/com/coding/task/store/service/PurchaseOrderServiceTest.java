package com.coding.task.store.service;

import com.coding.task.store.entity.LineItem;
import com.coding.task.store.entity.Product;
import com.coding.task.store.entity.PurchaseOrder;
import com.coding.task.store.model.Entry;
import com.coding.task.store.repository.LineItemRepository;
import com.coding.task.store.repository.ProductRepository;
import com.coding.task.store.repository.PurchaseOrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseOrderServiceTest {
    @Mock
    private LineItemRepository mockedLineItemRepository;

    @Mock
    private ProductRepository mockedProductRepository;

    @Mock
    private PurchaseOrderRepository mockedPurchaseOrderRepository;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    private Product apple;
    private Product orange;
    private PurchaseOrder mockedPurchaseOrder;

    @Before
    public void init() {
        apple = new Product();
        apple.setDescription("Apple");
        apple.setPrice(0.6);

        orange = new Product();
        orange.setDescription("Orange");
        orange.setPrice(0.25);

        mockedPurchaseOrder = new PurchaseOrder();
        mockedPurchaseOrder.setId(1L);

        this.purchaseOrderService = new PurchaseOrderService(mockedLineItemRepository, mockedProductRepository, mockedPurchaseOrderRepository);
    }

    @Test
    public void anOrderWithInvalidItemThrowsIllegalArgumentException() {
        String type = "lemon";
        Entry invalidEntry = new Entry(type, 1);

        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("lemon")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            purchaseOrderService.createPurchaseOrder(Collections.singletonList(invalidEntry));
        });

        String expectedMessage = "Invalid argument. " + type + " is not a valid item.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void anOrderWithOneAppleContainsOneItemAndTotalPriceIsAppleValue() {
        Entry oneApple = new Entry("apple", 1);

        LineItem mockedLineItem = new LineItem();
        mockedLineItem.setLineItemId(1L);
        mockedLineItem.setProduct(apple);

        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("apple")).thenReturn(apple);
        Mockito.when(mockedPurchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(mockedPurchaseOrder);
        Mockito.when(mockedLineItemRepository.save(any(LineItem.class))).thenReturn(mockedLineItem);

        com.coding.task.store.model.PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(Collections.singletonList(oneApple));

        assertEquals(1, purchaseOrder.getLineItems().size());
        assertEquals(apple.getPrice(), purchaseOrder.getValue());
    }

    @Test
    public void anOrderWithOneAppleAndOneOrangeContainsTwoItemsAndTotalPriceIsTheSumOfBothValues() {
        Entry oneApple = new Entry("apple", 1);
        Entry oneOrange = new Entry("orange", 1);

        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("apple")).thenReturn(apple);
        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("orange")).thenReturn(orange);
        Mockito.when(mockedLineItemRepository.save(any(LineItem.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(mockedPurchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(mockedPurchaseOrder);

        com.coding.task.store.model.PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(Arrays.asList(oneApple, oneOrange));

        assertEquals(2,purchaseOrder.getLineItems().size());
        assertEquals(apple.getPrice() + orange.getPrice(), purchaseOrder.getValue());
    }

    @Test
    public void orderWithTwoItemsWithQuantityGreaterThanOne() {
        int totalApples = 2;
        int totalOranges = 3;
        Entry twoApples = new Entry("apple", totalApples);
        Entry threeOranges = new Entry("orange", totalOranges);

        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("apple")).thenReturn(apple);
        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("orange")).thenReturn(orange);
        Mockito.when(mockedLineItemRepository.save(any(LineItem.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(mockedPurchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(mockedPurchaseOrder);

        com.coding.task.store.model.PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(Arrays.asList(twoApples, threeOranges));

        assertEquals(2, purchaseOrder.getLineItems().size());

        double totalApplesValue = apple.getPrice() * totalApples;
        double totalOrangesValue = orange.getPrice() * totalOranges;

        assertEquals(totalApplesValue + totalOrangesValue, purchaseOrder.getValue());
    }

    @Test
    public void anOrderWithItemsWithQuantityLesserThanOneExcludesTheItem() {
        int totalApples = -1;
        int totalOranges = 3;
        Entry minusOneApple = new Entry("apple", totalApples);
        Entry threeOranges = new Entry("orange", totalOranges);

        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("apple")).thenReturn(apple);
        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("orange")).thenReturn(orange);
        Mockito.when(mockedLineItemRepository.save(any(LineItem.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(mockedPurchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(mockedPurchaseOrder);

        com.coding.task.store.model.PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(Arrays.asList(minusOneApple, threeOranges));

        assertEquals(1, purchaseOrder.getLineItems().size());

        double totalOrangesValue = orange.getPrice() * totalOranges;
        assertEquals(totalOrangesValue, purchaseOrder.getValue());
    }

    @Test
    public void anOrderWithoutItemThrowsIllegalStateException() {
        String type = "orange";
        Entry onePositiveEntry = new Entry(type, 1);
        Entry oneNegativeEntry = new Entry(type, -1);

        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("orange")).thenReturn(orange);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            purchaseOrderService.createPurchaseOrder(Arrays.asList(onePositiveEntry, oneNegativeEntry));
        });

        String expectedMessage = "Order doesn't contain any items.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void orderWithTwoItemsWithQuantityGreaterThanOneApplyingOffer() {
        int totalApples = 15;
        int totalOranges = 20;
        Entry twoApples = new Entry("apple", totalApples);
        Entry threeOranges = new Entry("orange", totalOranges);

        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("apple")).thenReturn(apple);
        Mockito.when(mockedProductRepository.findByDescriptionIgnoreCase("orange")).thenReturn(orange);
        Mockito.when(mockedLineItemRepository.save(any(LineItem.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(mockedPurchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(mockedPurchaseOrder);

        com.coding.task.store.model.PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(Arrays.asList(twoApples, threeOranges));

        assertEquals(2, purchaseOrder.getLineItems().size());

        int appleQuantityToGetPrice = (totalApples / 3) * 2 + totalApples % 3;
        int orangeQuantityToGetPrice = totalOranges - totalOranges / 2;

        double totalApplesValue = apple.getPrice() * totalApples;
        double totalOrangesValue = orange.getPrice() * totalOranges;
        double totalValue = totalApplesValue + totalOrangesValue;

        assertEquals(totalValue, purchaseOrder.getValue());

        double totalApplesValueWithDiscount = apple.getPrice() * appleQuantityToGetPrice;
        double totalOrangesValueWithDiscount = orange.getPrice() * orangeQuantityToGetPrice;
        double totalValueWithDiscount = totalApplesValueWithDiscount + totalOrangesValueWithDiscount;

        assertEquals(totalValueWithDiscount, purchaseOrder.getTotalValueAfterDiscount());

        assertEquals(totalValue - totalValueWithDiscount, purchaseOrder.getDiscount());
    }

}