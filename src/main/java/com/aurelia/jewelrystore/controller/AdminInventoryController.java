package com.aurelia.jewelrystore.controller;

import com.aurelia.jewelrystore.model.JewelryItem;
import com.aurelia.jewelrystore.repository.JewelryItemRepository;
import com.aurelia.jewelrystore.service.JewelryInventoryService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminInventoryController {

    private final JewelryItemRepository repository;
    private final JewelryInventoryService inventoryService;
    private static final String UPLOAD_DIR = "./uploads";

    public AdminInventoryController(JewelryItemRepository repository, JewelryInventoryService inventoryService) {
        this.repository = repository;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("item", new JewelryItem());
        model.addAttribute("items", repository.findAll());
        model.addAttribute("categories", inventoryService.categories());
        return "admin";
    }

    @PostMapping("/items")
    public String saveItem(@Valid @ModelAttribute("item") JewelryItem item,
                           BindingResult result,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("items", repository.findAll());
            model.addAttribute("categories", inventoryService.categories());
            return "admin";
        }

        // Handle File Processing Logic
        if (!imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique file name to avoid collisions
                String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(imageFile.getInputStream(), filePath);

                // Save accessible public web directory URL path to database entry
                item.setImageUrl("/uploads/" + uniqueFileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Optional: handle visual fallback notifications here
            }
        } else if (item.getId() != null) {
            // Keep existing image if updating an item without selecting a new file
            JewelryItem existing = repository.findById(item.getId()).orElse(null);
            if (existing != null) {
                item.setImageUrl(existing.getImageUrl());
            }
        }

        repository.save(item);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        JewelryItem item = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("item", item);
        model.addAttribute("items", repository.findAll());
        model.addAttribute("categories", inventoryService.categories());
        return "admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/admin";
    }
    
    @PostMapping("/reset")
    public String resetDemo() {
        // Implementation fallback if running reset profiles
        return "redirect:/admin";
    }
}