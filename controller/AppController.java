package com.example.shopping.controller;

import com.example.shopping.model.Product;
import com.example.shopping.service.ContextHolderService;
import com.example.shopping.service.MainService;
import com.example.shopping.model.AppUser;
import com.example.shopping.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class AppController {

    private final MainService mainService;
    private final AppUserService appUserService;
    private final ContextHolderService contextHolderService;

    @Autowired
    public AppController(MainService mainService,
                         AppUserService appUserService,
                         ContextHolderService contextHolderService) {
        this.mainService = mainService;
        this.appUserService = appUserService;
        this.contextHolderService = contextHolderService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return mainService.selectAllProducts();
    }

    @GetMapping(path = "{productId}")
    public Product getProductById(@PathVariable("productId") int productId) {
        return mainService.selectProductById(productId);
    }

    @PostMapping(path = "{productId}")
    public Product saveProduct(@PathVariable("productId") int productId) {
        return mainService.saveProduct(productId, contextHolderService.getUserUsername());
    }

    @DeleteMapping(path = "{productId}")
    public String deleteProduct(@PathVariable("productId") int productId) {
        return mainService.deleteProduct(productId, contextHolderService.getUserUsername());
    }

    @GetMapping(path = "favorite")
    public List<Product> getFavouriteProducts() {
        return mainService.selectFavouriteProducts(contextHolderService.getUserUsername());
    }

    @PostMapping(path = "user/register")
    public void register(@RequestBody AppUser appUser) {
        appUserService.registerNewUser(appUser);
    }

    @GetMapping(path = "success")
    public String loginPage() {
        return "<div style=\"text-align:center\">" +
                "<h1 style=\"color:green\">Success Login Page</h1><div>";

    }

    @PostMapping("add/{productId}")
    public String addProductToCart(@PathVariable("productId") Integer productId) {
        return mainService.addProductToCart(productId);
    }

    @DeleteMapping("remove/{productId}")
    public String removeProductFromCart(@PathVariable("productId") Integer productId) {
        return mainService.deleteProductFromCart(productId);
    }

    @GetMapping("cart")
    public Map<Product, Integer> showCart() {
        return mainService.showCart();
    }

    @GetMapping("cart/price")
    public double getCartPrice() {
        return mainService.selectCartPrice();
    }

    @PostMapping("cart/buy")
    public String buyCartProducts() {
        return mainService.buyCartProducts(contextHolderService.getUserUsername());
    }


}
