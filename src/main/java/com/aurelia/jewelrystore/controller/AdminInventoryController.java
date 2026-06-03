package com.aurelia.jewelrystore.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aurelia.jewelrystore.model.JewelryItem;
import com.aurelia.jewelrystore.repository.JewelryItemRepository;
import com.aurelia.jewelrystore.service.JewelryInventoryService;

import jakarta.validation.Valid;

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
    public String saveItem(@Valid @ModelAttribute("item") JewelryItem item, BindingResult result,
                           @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("items", repository.findAll());
            model.addAttribute("categories", inventoryService.categories());
            return "admin";
        }

        if (!imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                item.setImageUrl("/uploads/" + uniqueFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (item.getId() != null) {
            repository.findById(item.getId()).ifPresent(old -> item.setImageUrl(old.getImageUrl()));
        }

        repository.save(item);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("item", repository.findById(id).orElseThrow());
        model.addAttribute("items", repository.findAll());
        model.addAttribute("categories", inventoryService.categories());
        return "admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/admin";
    }
}