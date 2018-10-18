package com.sda.smartCalendar.controller;

import com.sda.smartCalendar.controller.modelDTO.EventDTO;
import com.sda.smartCalendar.controller.modelDTO.UserDTO;
import com.sda.smartCalendar.controller.modelDTO.UserRegistrationDTO;
import com.sda.smartCalendar.domain.model.User;
import com.sda.smartCalendar.domain.repository.UserRepository;
import com.sda.smartCalendar.service.MappingService;
import com.sda.smartCalendar.service.UserService;
import com.sda.smartCalendar.social.providers.FacebookProvider;
import com.sda.smartCalendar.social.providers.GoogleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;


@Controller
public class UserController {

    @Autowired
    private FacebookProvider facebookProvider;

    @Autowired
    private GoogleProvider googleProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MappingService mappingService;

    @RequestMapping(value = "/facebook", method = RequestMethod.GET)
    public String loginToFacebook(Model model) {
        return facebookProvider.getFacebookUserData(model, new User());
    }

    @RequestMapping(value = "/google", method = RequestMethod.GET)
    public String loginToGoogle(Model model) {
        return googleProvider.getGoogleUserData(model, new User());
    }

    @RequestMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistration(UserRegistrationDTO userRegistrationDTO) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(HttpServletResponse httpServletResponse, @Valid UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        model.addAttribute("loggedInUser", userRegistrationDTO);
        userService.registerUser(userRegistrationDTO);
        return "redirect:/login";
    }

    @GetMapping("/index")
    public String showMainPage(Model model, Principal principal) {
        model.addAttribute("loggedInUser", userService.findByEmail(principal.getName()));
        return "index";
    }


    @GetMapping("/nopage")
    public String nopage() {
        return "nopage";
    }


    /**
     * If we can't find a user/email combination
     */
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    //?
    @ModelAttribute("loggedInUser")
    public void secureUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName());
        model.addAttribute("loggedInUser", user);
    }

}
