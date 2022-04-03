package com.virtuslab.internship.discount;

import com.virtuslab.internship.Controller;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BothDiscountsTest {
    @Test
    public void shouldApplyBothDiscountsIf3GrainAndThenPriceAbove50(){
        // Given
        var productDb = new ProductDb();
        var pork = productDb.getProduct("Pork").get();
        var bread = productDb.getProduct("Bread").get();
        var cereal = productDb.getProduct("Cereals").get();
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(pork, 5));
        receiptEntries.add(new ReceiptEntry(bread, 2));
        receiptEntries.add(new ReceiptEntry(cereal, 1));

        var receipt = new Receipt(receiptEntries);
        var tenPercentDiscount = new TenPercentDiscount();
        var fifteenPercentDiscount = new FifteenPercentDiscount();
        var expectedTotalPrice = pork.price().multiply(BigDecimal.valueOf(5))
                .add(bread.price().multiply(BigDecimal.valueOf(2)))
                .add(cereal.price().multiply(BigDecimal.valueOf(1)))
                .multiply(BigDecimal.valueOf(0.85))
                .multiply(BigDecimal.valueOf(0.9));

        // When
        var receiptAfterDiscount = tenPercentDiscount.apply(fifteenPercentDiscount.apply(receipt));
        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(2, receiptAfterDiscount.discounts().size());
    }

    @Test
    public void shouldNotApply10PercentIfPriceAfter15PercentIsUnder50(){
        // Given
        var productDb = new ProductDb();
        var pork = productDb.getProduct("Pork").get();
        var bread = productDb.getProduct("Bread").get();
        var cereal = productDb.getProduct("Cereals").get();
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(pork, 2));
        receiptEntries.add(new ReceiptEntry(bread, 2));
        receiptEntries.add(new ReceiptEntry(cereal, 1));

        var receipt = new Receipt(receiptEntries);
        var tenPercentDiscount = new TenPercentDiscount();
        var fifteenPercentDiscount = new FifteenPercentDiscount();
        var expectedTotalPrice = pork.price().multiply(BigDecimal.valueOf(2))
                .add(bread.price().multiply(BigDecimal.valueOf(2)))
                .add(cereal.price().multiply(BigDecimal.valueOf(1)))
                .multiply(BigDecimal.valueOf(0.85));

        // When
        var receiptAfterDiscount = tenPercentDiscount.apply(fifteenPercentDiscount.apply(receipt));

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }
}
