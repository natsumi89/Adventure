package com.example.Adventure;

import com.example.Adventure.domain.ShoppingCartsDetail;
import com.example.Adventure.domain.Users;
import com.example.Adventure.service.ShoppingCartsService;
import com.example.Adventure.service.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsersService usersService;
    private final ShoppingCartsService shoppingCartsService;

    @Autowired
    public CustomAuthenticationSuccessHandler(UsersService usersService, ShoppingCartsService shoppingCartsService) {
        this.usersService = usersService;
        this.shoppingCartsService = shoppingCartsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Users authenticatedUser = usersService.findByEmail(email);

        HttpSession session = request.getSession();

        session.setAttribute("email", authenticatedUser.getEmail());
        session.setAttribute("lastName", authenticatedUser.getLastName());
        session.setAttribute("userId", authenticatedUser.getUserId());

        List<ShoppingCartsDetail> cartProductsList = shoppingCartsService.findShoppingCartsDetailByUserId(authenticatedUser.getUserId());
        session.setAttribute("cartProductsList", cartProductsList);

        response.sendRedirect("/top/products");
    }
}