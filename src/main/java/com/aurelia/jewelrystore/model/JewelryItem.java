package com.aurelia.jewelrystore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class JewelryItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Product name is required")
  private String name;

  @NotBlank(message = "Category is required")
  private String category;

  @NotBlank(message = "Price is required")
  private String price;

  private String imageUrl;

  @NotBlank(message = "Description is required")
  private String description;

  public JewelryItem() {}

  public JewelryItem(Long id, String name, String category, String price, String imageUrl, String description) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.price = price;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }
  public String getPrice() { return price; }
  public void setPrice(String price) { this.price = price; }
  public String getImageUrl() { return imageUrl; }
  public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
}