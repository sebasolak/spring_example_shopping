package com.example.shopping.mockdata;

import com.example.shopping.model.Product;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MockData {

    public static ImmutableList<Product> getProducts() {
        try {
            InputStream inputStream = Resources.getResource("products_list.json").openStream();
            String json = IOUtils.toString(inputStream);
            Type listType = new TypeToken<ArrayList<Product>>() {
            }.getType();
            List<Product> products = new Gson().fromJson(json, listType);
            return ImmutableList.copyOf(products);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
