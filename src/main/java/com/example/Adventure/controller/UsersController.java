package com.example.Adventure.controller;

import com.example.Adventure.domain.Products;
import com.example.Adventure.domain.ShoppingCartsDetail;
import com.example.Adventure.domain.Users;
import com.example.Adventure.form.UsersForm;
import com.example.Adventure.repository.UsersRepository;
import com.example.Adventure.service.ProductsService;
import com.example.Adventure.service.ShoppingCartsService;
import com.example.Adventure.service.UsersService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersRepository.class);
    @Autowired
    private UsersService usersService;
    @Autowired
    private HttpSession session;
    @Autowired
    private ShoppingCartsService shoppingCartsService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("usersForm", new UsersForm());
        if (error != null) {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが間違っています。");
        }
        return "login";
    }

    @GetMapping("/customer-registration")
    public String register(Model model, UsersForm usersForm) {
        model.addAttribute("usersForm", new UsersForm());
        return "registration";
    }

    @PostMapping("/customer-insert")
    public String insert(@Valid UsersForm usersForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // バリデーションエラーがある場合、ログにエラーメッセージを出力してみる
            for (FieldError error : bindingResult.getFieldErrors()) {
                System.out.println(error.getField() + ": " + error.getDefaultMessage());
            }
            model.addAttribute("usersForm", usersForm);
            return "registration";
        }


        Users users = usersService.findByEmail(usersForm.getEmail());
        if (users != null) {
            model.addAttribute("error", "このメールアドレスは既に登録されています。");
            return "registration";
        }

        Users newUser = new Users();
        newUser.setFirstName(usersForm.getFirstName());
        newUser.setLastName(usersForm.getLastName());
        newUser.setBirthDate(usersForm.getBirthDate());
        newUser.setEmail(usersForm.getEmail());
        newUser.setPassword(usersForm.getPassword());
        String encodedPassword = passwordEncoder.encode(usersForm.getPassword());
        System.out.println("Encoded Password: " + encodedPassword);
        newUser.setPassword(encodedPassword);

        usersService.insert(newUser);

        return "redirect:/top/products";
    }


    @PostMapping("/login-to-list")
    public String customerLogin(@Validated UsersForm usersForm, BindingResult bindingResult, @ModelAttribute Users users, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("usersForm", usersForm);
            return "login";
        }

        Users authenticatedUser = usersService.findByEmail(usersForm.getEmail());
        List<ShoppingCartsDetail> sessionCartDetailsList = (List<ShoppingCartsDetail>) session.getAttribute("cartDetailsList");
        if (sessionCartDetailsList != null) {
            for (ShoppingCartsDetail detail : sessionCartDetailsList) {
                shoppingCartsService.updateOrAddToCart(authenticatedUser.getUserId(), detail.getProductId(), detail.getQuantity());
            }
            session.removeAttribute("cartDetailsList");
        }

        session.setAttribute("email", authenticatedUser.getEmail());
        session.setAttribute("lastName", authenticatedUser.getLastName());
        session.setAttribute("firstName", authenticatedUser.getFirstName());
        session.setAttribute("userId", authenticatedUser.getUserId());
        List<Products> sessionCartProductsList = (List<Products>) session.getAttribute("cartProductsList");
        if (sessionCartProductsList != null) {
            for (Products product : sessionCartProductsList) {
                shoppingCartsService.updateOrAddToCart(authenticatedUser.getUserId(), product.getProductId(), 1); // 仮に数量を1としています
            }
            session.removeAttribute("cartProductsList");
        }

        return "redirect:/top/products";
    }

    @PostMapping("/logout")
    public String logout() {
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
        session.invalidate();
        return "redirect:/top/products";
    }
}
