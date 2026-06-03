package com.aurelia.jewelrystore.config;

import com.aurelia.jewelrystore.service.JewelryInventoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataLoader implements CommandLineRunner {

  private final JewelryInventoryService inventoryService;

  public DemoDataLoader(JewelryInventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @Override
  public void run(String... args) {
    inventoryService.seedDemoItemsIfEmpty();
  }
}
