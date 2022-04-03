package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptGenerator {

    public Receipt generate(Basket basket) {
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        Map<Product, Integer> products = new HashMap<>();
        for(Product product: basket.getProducts()){
            if(products.containsKey(product)){
                products.put(product, products.get(product) + 1);
            }else{
                products.put(product, 1);
            }
        }
        for(Product product: products.keySet()){
            receiptEntries.add(new ReceiptEntry(product, products.get(product)));
        }
        return new Receipt(receiptEntries);
    }
}
