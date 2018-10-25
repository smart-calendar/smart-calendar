package com.sda.smartCalendar.controller;

import com.sda.smartCalendar.controller.modelDTO.EventDTO;
import com.sda.smartCalendar.controller.modelDTO.UserDTO;
import com.sda.smartCalendar.controller.modelDTO.UserRegistrationDTO;
import com.sda.smartCalendar.domain.IUserService;
import com.sda.smartCalendar.domain.OnRegistrationCompleteEvent;
import com.sda.smartCalendar.domain.model.User;
import com.sda.smartCalendar.domain.model.VerificationToken;
import com.sda.smartCalendar.domain.repository.UserRepository;
import com.sda.smartCalendar.service.MappingService;
import com.sda.smartCalendar.service.UserService;
import com.sda.smartCalendar.social.providers.FacebookProvider;
import com.sda.smartCalendar.social.providers.GoogleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.Locale;
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
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private IUserService service;

    @Autowired
    private MessageSource messages;

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

    @GetMapping("/logout")
    public String logout() {
        return "logout";
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
    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("userRegistrationDTO") @Valid UserRegistrationDTO userRegistrationDTO,
                               BindingResult bindingResult, WebRequest request) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (userService.findByEmail(userRegistrationDTO.getEmail()) != null) {
            return "redirect:/registration?failed";
        }

        User registered = userService.registerUser(userRegistrationDTO);
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent
                (registered, request.getLocale(), appUrl));
        return "redirect:/registration?success";
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {
        Locale locale = request.getLocale();
        VerificationToken verificationToken = service.getVerificationToken(token);
        if (verificationToken == null) {
            return "redirect:/badUser?invalidToken";
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "redirect:/badUser?expired";
        }
        user.setEnabled(true);
        service.saveRegisteredUser(user);
        return "redirect:/login?confirm";
    }

    @GetMapping("/terms")
    public String showTerms() {
        return "terms";
    }

    @GetMapping("/badUser")
    public String badUser() {
        return "badUser";
    }

    @GetMapping("/forgot")
    public String forgotPassword() {
        return "forgot";
    }


    @PostMapping("/forgot")
    public String sendPassword() {
        return "nopage";
    }

    @GetMapping("/profile")
    public String showProfie(Model model, Principal principal){
        model.addAttribute("userProfile",userService.findUserByEmail(principal.getName()));
        return "profile";
    }

    @GetMapping("/editUserProfile")
    public String editUserProfile(Model model, Principal principal, @RequestParam("typedFields") String typedFields){

        if (typedFields.equals("firstName")) {
            model.addAttribute("userProfile", userService.findUserByEmail(principal.getName()));
            model.addAttribute("userField", "firstName");
        }
        if (typedFields.equals("lastName")) {
            model.addAttribute("userProfile", userService.findUserByEmail(principal.getName()));
            model.addAttribute("userField", "lastName");
        }
        if (typedFields.equals("phoneNumber")) {
            model.addAttribute("userProfile", userService.findUserByEmail(principal.getName()));
            model.addAttribute("userField", "phoneNumber");
        }
        return "editProfile";
    }

    @PostMapping("/editUserProfile")
    public String updateUserProfile(Model model, Principal principal, UserRegistrationDTO userRegistrationDTO) {
        model.addAttribute("userProfile", userRegistrationDTO);
        userService.editProfile(userRegistrationDTO, principal);
        return "redirect:/profile";
    }
}