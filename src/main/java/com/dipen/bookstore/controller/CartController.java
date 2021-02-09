package com.dipen.bookstore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dipen.bookstore.books.dto.CartDto;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

    @GetMapping(value = "")
    public String cart(HttpSession session, Model model) {
        List<CartDto> cartList = checkAndRetrieveCartItemList(session);
        double subTotal = cartList.stream()
                .map(cartDto -> cartDto.getPrice() * cartDto.getQuantity())
                .reduce(0.0, Double::sum);
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("cartItems", cartList);
        return "cart";
    }

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<?> addItemToCart(@RequestBody CartDto cartItem, HttpSession session) {
        List<CartDto> cartList = checkAndRetrieveCartItemList(session);

        Optional<CartDto> prevSimilarItemInCart = cartList.stream()
                .filter(cartDto -> cartDto.getBookId() == cartItem.getBookId())
                .findFirst();

        if (!prevSimilarItemInCart.isPresent()) {
            cartList.add(cartItem);
        }

        session.setAttribute("cart", cartList);
        return new ResponseEntity<>("Cart Updated!", HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<CartDto> updateCartItem(@RequestBody CartDto cart, HttpSession session) {
        List<CartDto> cartList = checkAndRetrieveCartItemList(session);
        cartList = cartList.stream()
                .filter( cartDto -> {
                    if (cartDto.getBookId() == cart.getBookId())
                        cartDto.setQuantity(cart.getQuantity());
                    return true;
                }).collect(Collectors.toList());
        session.setAttribute("cart", cartList);
        return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
    }


    @DeleteMapping(value = "/remove/{bookId}")
    @ResponseBody
    public ResponseEntity<?> removeItemFromCart(@PathVariable int bookId, HttpSession session) {
        List<CartDto> cartList = checkAndRetrieveCartItemList(session);
        List<CartDto> newCartList = cartList.stream()
                .filter(cartDto -> cartDto.getBookId() != bookId)
                .collect(Collectors.toList());
        session.setAttribute("cart", newCartList);
        return new ResponseEntity<>("Item Removed From Cart!", HttpStatus.OK);
    }


    private List<CartDto> checkAndRetrieveCartItemList(HttpSession session) {
        List<CartDto> cartList = null;
        if (session.getAttribute("cart") == null) {
            cartList = new ArrayList<>();
        } else {
            cartList = (List<CartDto>) session.getAttribute("cart");
        }

        return cartList;
    }

}
