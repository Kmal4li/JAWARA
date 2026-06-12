package app.service;

import app.model.CategoryProduct;
import app.repository.CategoryRepository;
import java.util.List;

public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
    }

    public void addCategory(CategoryProduct category) throws Exception {
        if (category.getNamaKategori() == null || category.getNamaKategori().trim().isEmpty()) {
            throw new Exception("Category name is required");
        }
        categoryRepository.insert(category);
    }

    public void updateCategory(CategoryProduct category) throws Exception {
        if (category.getNamaKategori() == null || category.getNamaKategori().trim().isEmpty()) {
            throw new Exception("Category name is required");
        }
        categoryRepository.update(category);
    }

    public void deleteCategory(int id) throws Exception {
        categoryRepository.delete(id);
    }

    public List<CategoryProduct> getAllCategories() {
        return categoryRepository.findAll();
    }
}
