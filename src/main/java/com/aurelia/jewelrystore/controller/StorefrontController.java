package com.aurelia.jewelrystore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aurelia.jewelrystore.service.JewelryInventoryService;

@Controller
public class StorefrontController {

  private final JewelryInventoryService inventoryService;

  public StorefrontController(JewelryInventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping("/")
  public String home(@RequestParam(defaultValue = "All") String category, Model model) {
    model.addAttribute("items", inventoryService.findByCategory(category));
    model.addAttribute("categories", inventoryService.categories());
    model.addAttribute("activeCategory", category);
    return "storefront";
  }
}