package com.aurelia.jewelrystore.controller;

import com.aurelia.jewelrystore.model.JewelryItem;
import com.aurelia.jewelrystore.service.JewelryInventoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final JewelryInventoryService inventoryService;

  public AdminController(JewelryInventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping
  public String admin(Model model) {
    if (!model.containsAttribute("item")) {
      model.addAttribute("item", new JewelryItem());
    }
    model.addAttribute("items", inventoryService.findAll());
    model.addAttribute("categories", categories());
    return "admin";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    return inventoryService.findById(id)
        .map(item -> {
          model.addAttribute("item", item);
          model.addAttribute("items", inventoryService.findAll());
          model.addAttribute("categories", categories());
          return "admin";
        })
        .orElseGet(() -> {
          redirectAttributes.addFlashAttribute("message", "Item was not found.");
          return "redirect:/admin";
        });
  }

  @PostMapping("/items")
  public String save(@Valid @ModelAttribute("item") JewelryItem item, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("items", inventoryService.findAll());
      model.addAttribute("categories", categories());
      return "admin";
    }

    inventoryService.save(item);
    return "redirect:/admin";
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    inventoryService.delete(id);
    return "redirect:/admin";
  }

  @PostMapping("/reset")
  public String reset() {
    inventoryService.resetDemoItems();
    return "redirect:/admin";
  }

  private String[] categories() {
    return new String[] {"Earrings", "Bangles", "Chains", "Rings", "Hair Clips", "Anklets", "Combo Sets"};
  }
}
