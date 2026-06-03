package com.aurelia.jewelrystore.service;

import com.aurelia.jewelrystore.model.JewelryItem;
import com.aurelia.jewelrystore.repository.JewelryItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JewelryInventoryService {

  private final JewelryItemRepository repository;

  public JewelryInventoryService(JewelryItemRepository repository) {
    this.repository = repository;
  }

  public List<JewelryItem> findAll() {
    return repository.findAll().stream().sorted((left, right) -> right.getId().compareTo(left.getId())).toList();
  }

  public List<JewelryItem> findByCategory(String category) {
    if (category == null || category.isBlank() || category.equalsIgnoreCase("All")) {
      return findAll();
    }

    return repository.findByCategoryIgnoreCaseOrderByIdDesc(category);
  }

  public List<String> categories() {
    List<String> categories = new ArrayList<>();
    categories.add("All");
    repository.findAll().stream().map(JewelryItem::getCategory).distinct().sorted().forEach(categories::add);
    return categories;
  }

  public Optional<JewelryItem> findById(Long id) {
    return repository.findById(id);
  }

  public void save(JewelryItem item) {
    repository.save(item);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Transactional
  public void seedDemoItemsIfEmpty() {
    if (repository.count() == 0) {
      addDemoItems();
    }
  }

  @Transactional
  public void resetDemoItems() {
    repository.deleteAll();
    addDemoItems();
  }

  private void addDemoItems() {
    save(new JewelryItem(null, "Daily Wear Hoop Earrings", "Earrings", "₹149",
        "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?auto=format&fit=crop&w=900&q=80",
        "Lightweight fancy earrings for office, college, and everyday use."));
    save(new JewelryItem(null, "Rolled Gold Style Bangles", "Bangles", "₹249",
        "https://images.unsplash.com/photo-1611591437281-460bfbe1220a?auto=format&fit=crop&w=900&q=80",
        "Simple rolled-gold look bangles for daily wear. Imitation jewelry, not real gold."));
    save(new JewelryItem(null, "Simple Chain Set", "Chains", "₹199",
        "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?auto=format&fit=crop&w=900&q=80",
        "Easy-to-style fashion chain set with a clean everyday finish."));
    save(new JewelryItem(null, "Adjustable Fancy Ring", "Rings", "₹99",
        "https://images.unsplash.com/photo-1603561596112-db1d6d6b1f2b?auto=format&fit=crop&w=900&q=80",
        "Budget-friendly adjustable ring for casual outfits and small gifting."));
    save(new JewelryItem(null, "Pearl Hair Clip Pair", "Hair Clips", "₹129",
        "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=900&q=80",
        "Cute fancy hair clips for daily styling, parties, and school events."));
    save(new JewelryItem(null, "Anklet Combo Pack", "Anklets", "₹179",
        "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?auto=format&fit=crop&w=900&q=80",
        "Simple fashion anklets sold as a combo pack for regular wear."));
  }
}
