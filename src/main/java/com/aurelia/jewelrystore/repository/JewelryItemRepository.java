package com.aurelia.jewelrystore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurelia.jewelrystore.model.JewelryItem;

public interface JewelryItemRepository extends JpaRepository<JewelryItem, Long> {
  List<JewelryItem> findByCategoryIgnoreCaseOrderByIdDesc(String category);
}