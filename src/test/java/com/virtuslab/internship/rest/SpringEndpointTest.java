package com.virtuslab.internship.rest;

import com.virtuslab.internship.Controller;
import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SpringEndpointTest {

    @Autowired
    private Controller controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void addedProductsShouldAppearInBasket(){
        // Given

        controller = new Controller();
        Basket basket = new Basket();
        ProductDb productDb = new ProductDb();
        var bread = productDb.getProduct("Bread").get();
        var steak = productDb.getProduct("Steak").get();
        basket.addProduct(bread);
        basket.addProduct(steak);

        // When
        controller.addProducts(new String[]{"Bread", "Steak"});

        // Then
        assertEquals(basket.getProducts(), controller.getBasket().getProducts());
    }

    @Test
    public void receiptShouldBeGenerated(){
        // Given

        controller = new Controller();
        ReceiptGenerator generator = new ReceiptGenerator();
        Basket basket = new Basket();
        ProductDb productDb = new ProductDb();
        var bread = productDb.getProduct("Bread").get();
        var steak = productDb.getProduct("Steak").get();
        basket.addProduct(bread);
        basket.addProduct(steak);
        TenPercentDiscount tenPercentDiscount = new TenPercentDiscount();
        FifteenPercentDiscount fifteenPercentDiscount = new FifteenPercentDiscount();
        var receipt = tenPercentDiscount.apply(fifteenPercentDiscount.apply(generator.generate(basket)));

        // When
        controller.addProducts(new String[]{"Bread", "Steak"});
        var generatedReceipt = controller.getReceipt();

        // Then
        assertEquals(receipt.entries(), generatedReceipt.entries());
        assertEquals(receipt.discounts(), generatedReceipt.discounts());
        assertEquals(receipt.totalPrice(), generatedReceipt.totalPrice());
    }
}
