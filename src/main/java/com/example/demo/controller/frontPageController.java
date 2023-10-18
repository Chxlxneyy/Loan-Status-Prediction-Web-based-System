package com.example.demo.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@CrossOrigin("*")
public class frontPageController {
	
	private static final Logger logger = Logger.getLogger(frontPageController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public String Index() {
		return "index";
	}

	@GetMapping("/admin/")
	public String dashboard(Model model) {
		model.addAttribute("user", userService.getAllUsers());
		return "dashboard";
	}

	@GetMapping("/admin/add")
	public String addUsers() {
		return "adduser";
	}

	@GetMapping("/admin/edit")
	public String editUsers() {
		return "edituser";
	}

	@GetMapping("/user/")
	public String home() {
		return "home";
	}

	@PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        try {
            User existingUser = userRepo.findByEmail(user.getEmail());

            if (existingUser != null) {
                session.setAttribute("msgError", "Email address already exists. Please use a different email.");
                session.removeAttribute("msg");

                // Log a WARN message for the existing user
                logger.warn("Email address already exists for user with email: " + user.getEmail());

                return "redirect:/";
            } else {
                User savedUser = userService.saveUser(user);

                // Log an INFO message for successfully saving a new user
                logger.info("Saved a new user with email: " + user.getEmail());
            }

            return "redirect:/";
        } catch (Exception e) {
            // Log the exception with an ERROR level
            logger.error("An error occurred while saving a new user", e);

            // Log a WARN message for the save failure
            logger.warn("Failed to save a new user");

            return "redirect:/errorPage";
        }
    }

	@PostMapping("/admin/user")
    public String saveProducts(@ModelAttribute("user") User user) {
        try {
            userService.saveUser(user);

            // Log an INFO message for successfully saving a new user by the admin
            logger.info("Saved a new user by admin with email: " + user.getEmail());

            return "redirect:/admin/";
        } catch (Exception e) {
            // Log the exception with an ERROR level
            logger.error("An error occurred while saving a new user by admin", e);

            // Log a WARN message for the save failure
            logger.warn("Failed to save a new user by admin");

            return "redirect:/errorPage";
        }
    }
	
	 @GetMapping("/admin/user/{id}")
	    public String deleteUsers(@PathVariable Long id) {
	        try {
	            userService.deleteUserByID(id);

	            // Log an INFO message for successfully deleting a user by the admin
	            logger.info("Deleted user by admin with ID: " + id);

	            return "redirect:/admin/";
	        } catch (Exception e) {
	            // Log the exception with an ERROR level
	            logger.error("An error occurred while deleting a user by admin with ID: " + id, e);

	            // Log a WARN message for the delete failure
	            logger.warn("Failed to delete a user by admin with ID: " + id);

	            return "redirect:/errorPage";
	        }
	    }
	
	 @GetMapping("/admin/user/edit/{id}")
	    public String editProducts(@PathVariable Long id, Model model) {
	        try {
	            User user = userService.getUsersById(id);
	            model.addAttribute("user", user);

	            // Log an INFO message for accessing the edit user page
	            logger.info("Accessed the edit user page for user with ID: " + id);

	            return "edituser";
	        } catch (Exception e) {
	            // Log the exception with an ERROR level
	            logger.error("An error occurred while accessing the edit user page for user with ID: " + id, e);

	            // Log a WARN message for the access failure
	            logger.warn("Failed to access the edit user page for user with ID: " + id);

	            return "redirect:/errorPage";
	        }
	    }
	
	 @PostMapping("/admin/user/{id}")
	    public String updateProduct(@PathVariable Long id, @ModelAttribute("user") User product, Model model) {
	        try {
	            User existingProduct = userService.getUsersById(id);
	            existingProduct.setId(id);
	            existingProduct.setName(product.getName());
	            existingProduct.setEmail(product.getEmail());
	            existingProduct.setPassword(product.getPassword());

	            userService.updateUser(existingProduct);

	            // Log an INFO message for successfully updating a user's information
	            logger.info("Updated user information with ID: " + id);

	            return "redirect:/admin/";
	        } catch (Exception e) {
	            // Log the exception with an ERROR level
	            logger.error("An error occurred while updating user information with ID: " + id, e);

	            // Log a WARN message for the update failure
	            logger.warn("Failed to update user information with ID: " + id);

	            return "redirect:/errorPage";
	        }
	    }

}
