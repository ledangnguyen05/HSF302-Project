package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.CategoryEntity;
import hsf302.hsf302project.repository.CategoryRepository;
import hsf302.hsf302project.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/categories")
@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/listCategory")
    public String listCategoryPage(Model model) {
        model.addAttribute("categoryList", categoryService.findAllCategories());
        return "category/categoryList";
    }

    @GetMapping("/createPage")
    public String createCategoryPage(Model model) {
        model.addAttribute("category", new CategoryEntity());
        return "category/addCategory";
    }

    @PostMapping("/createExecute")
    public String createCategoryExecute(@Valid @ModelAttribute("category") CategoryEntity category,
                                        Model model,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "category/addCategory";
        }

        if (categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName()).isPresent()) {
            model.addAttribute("error", "Category name is already taken");
            return "category/addCategory";
        }

        boolean isCreated = categoryService.addCategory(category);
        if (isCreated) {
            redirectAttributes.addFlashAttribute("message", "Category created successfully");
            return "redirect:/categories/listCategory";
        } else {
            model.addAttribute("error", "Failed to create category");
            return "category/addCategory";
        }
    }

    @GetMapping("/updatePage/{id}")
    public String updateCategoryPage(@PathVariable int id, Model model) {
        model.addAttribute("category", categoryService.findByCategoryId(id));
        return "category/updateCategory";
    }

    @PostMapping("/updateExecute")
    public String updateCategoryExecute(@Valid @ModelAttribute("category") CategoryEntity category,
                                        Model model,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "category/updateCategory";
        }

        if (categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName()).isPresent()) {
            model.addAttribute("error", "Category name is already taken");
            return "category/updateCategory";
        }

        boolean isupdated = categoryService.updateCategory(category.getId(), category);
        if (isupdated) {
            redirectAttributes.addFlashAttribute("message", "Category updated successfully");
            return "redirect:/categories/listCategory";
        } else {
            model.addAttribute("error", "Failed to updated category");
            return "category/updateCategory";
        }
    }

    @GetMapping("/deleteCategory/{categoryId}")
    public String deleteCategory(@PathVariable int categoryId,
                                 RedirectAttributes redirectAttributes) {
        boolean isDeleted = categoryService.deleteCategory(categoryId);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "Category deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete category");
        }
        return "redirect:/category/listCategory";
    }

    @PostMapping("/searchByName")
    public String searchCategoryByName(@RequestParam("categoryName") String categoryName,
                                       Model model) {
        List<CategoryEntity> list=categoryService.findByCategoryNameContaining(categoryName);
        if(list==null || list.isEmpty()){
            model.addAttribute("error", "Category name not found for :"+categoryName);
        }else {
            model.addAttribute("categoryList", list);
        }
        return "category/categoryList";
    }

}
