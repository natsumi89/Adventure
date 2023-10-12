package com.example.Adventure.controller;

import com.example.Adventure.domain.Products;
import com.example.Adventure.domain.Users;
import com.example.Adventure.service.ProductsService;
import com.example.Adventure.service.ShoppingCartsService;
import com.example.Adventure.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private HttpSession session;

    @Autowired
    private ShoppingCartsService shoppingCartsService;

    @Autowired
    private ProductsService productsService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-to-list")
    public String customerLogin(@ModelAttribute Users users, RedirectAttributes redirectAttributes) {
        Users authenticatedUser = usersService.findByEmailAndPassword(users.getEmail(),users.getPassword());

        if(authenticatedUser == null ) {
            redirectAttributes.addFlashAttribute("errorMessage","メールアドレスまたはパスワードが間違っています。");
            return "redirect:/top/products";
        }


        // セッションにユーザー情報を保存
        session.setAttribute("email", authenticatedUser.getEmail());
        session.setAttribute("lastName", authenticatedUser.getLastName());
        session.setAttribute("userId", authenticatedUser.getUserId());  // この行を確認

        List<Products> sessionCartProductsList = (List<Products>) session.getAttribute("cartProductsList");
        if(sessionCartProductsList != null) {
            for(Products product : sessionCartProductsList) {
                shoppingCartsService.updateOrAddToCart(authenticatedUser.getUserId(), product.getProductId(), 1); // 仮に数量を1としています
            }
            session.removeAttribute("cartProductsList"); // セッションのカートをクリア
        }

        return "redirect:/top/products";
    }
    @PostMapping("/logout")
    public String logout() {
        // ユーザーIDをセッションから取得
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            // セッションからカートの商品リストを取得
            List<Products> cartProductsList = (List<Products>) session.getAttribute("cartProductsList");
            if (cartProductsList != null) {
                Map<Integer, Integer> productCountMap = new HashMap<>();
                for (Products product : cartProductsList) {
                    productCountMap.put(product.getProductId(), productCountMap.getOrDefault(product.getProductId(), 0) + 1);
                }
                for (Map.Entry<Integer, Integer> entry : productCountMap.entrySet()) {
                    shoppingCartsService.updateOrAddToCart(userId, entry.getKey(), entry.getValue());
                }
            }
        }
        // セッションの情報をクリア
        session.invalidate();
        return "redirect:/top/products"; // ログインページへリダイレクト
    }
}
