package com.example.Adventure.controller;

import com.example.Adventure.domain.Users;
import com.example.Adventure.form.UsersForm;
import com.example.Adventure.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private HttpSession session;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-to-list")
    public String customerLogin(@ModelAttribute Users users,RedirectAttributes redirectAttributes) {
        Users authenticatedUser = usersService.findByEmailAndPassword(users.getEmail(),users.getPassword());

        if(authenticatedUser == null ) {
            redirectAttributes.addFlashAttribute("errorMessage","メールアドレスまたはパスワードが間違っています。");
            return "redirect:/top/products";
        }

        session.setAttribute("email",authenticatedUser.getEmail());
        session.setAttribute("lastName",authenticatedUser.getLastName());

        System.out.println("Authenticated User: " + authenticatedUser);

        return "redirect:/top/products";


    }

    @GetMapping("/customer-registration")
    public String register() {
        return "registration";
    }

    @PostMapping("/customer-insert")
    public String insert(UsersForm usersForm, Model model) {
        Users users = usersService.findByEmailAndPassword(usersForm.getEmail(), usersForm.getPassword());
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

        usersService.insert(newUser);

        return "redirect:/top/products";
    }


    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/top/products";
    }

//    @PostMapping("/delete")
//    public void delete(Integer userId) {
//        Users users = usersService.delete(usersForm.getUserId());
//    }
}
