package javaee.kononko.homework9.controllers;

import javaee.kononko.homework9.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RequiredArgsConstructor
@Controller
public class WishlistController {
    private final WishlistService wishlistService;

    @RequestMapping(value = "/wishlist/add", method = RequestMethod.POST)
    public ModelAndView addToWishlist(@RequestParam String id){
        var user = SecurityContextHolder.getContext().getAuthentication().getName();
        wishlistService.addBookToWishlist(user, Integer.parseInt(id));
        var redirect = new RedirectView("/book/" + id);
        redirect.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return new ModelAndView(redirect);
    }

    @RequestMapping(value = "/wishlist/remove", method = RequestMethod.POST)
    public ModelAndView removeFromWishlist(@RequestParam String id){
        var user = SecurityContextHolder.getContext().getAuthentication().getName();
        wishlistService.removeFromWishlist(user, Integer.parseInt(id));
        var redirect = new RedirectView("/book/" + id);
        redirect.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return new ModelAndView(redirect);
    }

    @RequestMapping("/wishlist")
    public String wishlistBooks(Model model){
        var user = SecurityContextHolder.getContext().getAuthentication().getName();
        var books = wishlistService.getWishlistBooks(user);
        model.addAttribute("books", books);
        return "wishlist";
    }
}
