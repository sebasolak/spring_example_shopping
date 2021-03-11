package com.example.shopping.service;

import com.example.shopping.mockdata.MockData;
import com.example.shopping.model.AppUser;
import com.example.shopping.model.BankAccount;
import com.example.shopping.model.Cart;
import com.example.shopping.model.Product;
import com.example.shopping.repo.BankAccountRepo;
import com.example.shopping.repo.CartRepo;
import com.example.shopping.repo.ProductRepo;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainService {
    static ImmutableList<Product> PRODUCTS = MockData.getProducts();

    private final ProductRepo productRepo;
    private final AppUserService appUserService;
    private final BankAccountRepo bankAccountRepo;
    private final CartRepo cartRepo;
    private Map<Product, Integer> productsMap = new HashMap<>();

    @Autowired
    public MainService(ProductRepo productRepo,
                       AppUserService appUserService,
                       BankAccountRepo bankAccountRepo,
                       CartRepo cartRepo) {
        this.productRepo = productRepo;
        this.appUserService = appUserService;
        this.bankAccountRepo = bankAccountRepo;
        this.cartRepo = cartRepo;
    }

    public List<Product> selectAllProducts() {
        return PRODUCTS;
    }

    public Product selectProductById(int id) {
        return PRODUCTS.stream().filter(product -> product.getId() == id)
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("Product %d didn't exist", id)));
    }

    @Transactional
    public Product saveProduct(int id, String username) {
        Product product = selectProductById(id);
        product.setAppUserUsername(username);
        if (productRepo.findProductByIdAndAppUserUsername(id, username) == null) {
            productRepo.save(product);
        }
        return product;
    }

    public String deleteProduct(int id, String username) {
        if (productRepo.findProductByIdAndAppUserUsername(id, username) == null) {
            throw new IllegalArgumentException(
                    String.format("Product %d %s didn't exist in database", id, username)
            );
        }
        productRepo.deleteById(id);
        return String.format("Product %d %s was deleted", id, username);
    }

    public List<Product> selectFavouriteProducts(String username) {
        return productRepo.findProductsByAppUserUsername(username);
    }

    public String addProductToCart(Integer productId) {
        Product product = selectProductById(productId);
        productsMap.put(product, productsMap.getOrDefault(product, 0) + 1);
        return String.format("Product %s added to cart", product.getName());
    }

    public String deleteProductFromCart(int productId) {
        Product product = selectProductById(productId);
        if (!productsMap.containsKey(product)) {
            throw new IllegalArgumentException(String.format("Product of id %d not exist in database", productId));
        }

        if (productsMap.get(product) > 1) {
            int val = productsMap.get(product) - 1;
            productsMap.put(product, val);
        } else {
            productsMap.remove(product);
        }

        return String.format("Deleted one product of id %d", productId);
    }

    public Map<Product, Integer> showCart() {
        return productsMap;
    }

    public double selectCartPrice() {
        return productsMap.entrySet().stream().mapToDouble(pair -> pair.getKey().getPrice() * pair.getValue()).sum();
    }

    @Transactional
    public String buyCartProducts(String username) {
        if (productsMap.isEmpty()) {
            throw new IllegalTransactionStateException("Cart is empty!");
        }
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);
        int appUserId = appUser.getId();
        double cartPrice = selectCartPrice();

        BankAccount bankAccount = bankAccountRepo.findByAppUserId(appUserId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Bank account with user id %d not exsist", appUserId))
                );

        if (bankAccount.getFounds().doubleValue() < cartPrice) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Cart cart = new Cart(appUser, productsMap, cartPrice);
        cartRepo.save(cart);
        productsMap.clear();

        BigDecimal updatedFounds = BigDecimal.valueOf(appUser.getAccountBalance().doubleValue() - cartPrice);
        appUser.setAccountBalance(updatedFounds);
        bankAccount.setFounds(updatedFounds);

        return "Thank you for choose our store!";
    }


}
