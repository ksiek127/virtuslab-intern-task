package com.virtuslab.internship;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    ProductDb db = new ProductDb();
    Basket basket = new Basket();
    TenPercentDiscount tenPercentDiscount = new TenPercentDiscount();
    FifteenPercentDiscount fifteenPercentDiscount = new FifteenPercentDiscount();

    @GetMapping("/product/")
    public Product getProduct(@RequestParam("name") String name){
        if(db.getProduct(name).isPresent()) {
            return db.getProduct(name).get();
        }
        return null;
    }

    @GetMapping("/receipt")
    public Receipt getReceipt(){
        ReceiptGenerator generator = new ReceiptGenerator();
        Receipt receipt = generator.generate(basket);
        return tenPercentDiscount.apply(fifteenPercentDiscount.apply(receipt));
    }

    @PostMapping("/add")
    public void addProducts(@RequestParam("names") String[] names){
        for(String name: names){
            var product = db.getProduct(name);
            product.ifPresent(value -> basket.addProduct(value));
        }
    }

    public Basket getBasket(){
        return basket;
    }
}
