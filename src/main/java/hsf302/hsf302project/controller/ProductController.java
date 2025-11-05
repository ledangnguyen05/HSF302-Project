package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.CategoryEntity;
import hsf302.hsf302project.entity.ProductEntity;
import hsf302.hsf302project.entity.SupplierEntity;
import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.service.CategoryService;
import hsf302.hsf302project.service.ProductService;
import hsf302.hsf302project.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/ListProducts")
    public ModelAndView listProducts() {
        List<ProductEntity> prodList = productService.getAllProducts();
        ModelAndView mav = new ModelAndView();
        mav.addObject("products", prodList);
        mav.setViewName("product/productList");
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addProductForm(HttpSession session) {
        List<CategoryEntity> cateList = categoryService.findAllCategories();
        ModelAndView mav = new ModelAndView();
        mav.addObject("productEntity", new ProductEntity());
        mav.addObject("categories", cateList);
        mav.addObject("suppliers", supplierService.getAll());
        mav.setViewName("product/addProduct");
        return mav;
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("productEntity") ProductEntity productEntity,
                             BindingResult bindingResult,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @RequestParam int categoryId,
                             @RequestParam int supplierId,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("suppliers", supplierService.getAll());
            return "product/addProduct";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = "D:/FlowerImg/";
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                String originalFilename = imageFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String uniqueFilename = UUID.randomUUID().toString() + extension;
                Path filePath = Paths.get(uploadDir + uniqueFilename);

                Files.write(filePath, imageFile.getBytes());

                productEntity.setImagePath(uniqueFilename);

            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload image: " + e.getMessage());
                model.addAttribute("categories", categoryService.findAllCategories());
                model.addAttribute("suppliers", supplierService.getAll());
                return "product/addProduct";
            }
        }


        productEntity.setCategory(categoryService.findByCategoryId(categoryId));
        productEntity.setSupplier(supplierService.findById(supplierId));

        productService.addProd(productEntity);

        return "redirect:/products/ListProducts";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        productService.deleteProd(id);
        redirectAttributes.addFlashAttribute("message", "Product added successfully");
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully");
        return "redirect:/products/ListProducts";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProductForm(@PathVariable int id, HttpSession session) {
        ProductEntity productEntity = productService.findById(id);
        if (productEntity == null) {
            return new ModelAndView("redirect:/products/ListProducts");
        }
        List<CategoryEntity> cateList = categoryService.findAllCategories();
        ModelAndView mav = new ModelAndView();
        mav.addObject("productEntity", productEntity);
        mav.addObject("categories", cateList);
        mav.addObject("suppliers", supplierService.getAll());
        mav.setViewName("product/editProduct");
        return mav;
    }

    @PostMapping("/edit")
    public String editProduct(@Valid @ModelAttribute("productEntity") ProductEntity productEntity,
                              BindingResult bindingResult,
                              Model model,
                              @RequestParam("categoryId") int categoryId,
                              @RequestParam("supplierId") int supplierId,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            List<CategoryEntity> cateList = categoryService.findAllCategories();
            model.addAttribute("productEntity", productEntity);
            model.addAttribute("categories", cateList);
            model.addAttribute("suppliers", supplierService.getAll());
            return "product/editProduct";
        }

        // Gán category & supplier
        CategoryEntity categoryEntity = categoryService.findByCategoryId(categoryId);
        SupplierEntity supplierEntity = supplierService.findById(supplierId);
        productEntity.setCategory(categoryEntity);
        productEntity.setSupplier(supplierEntity);

        // Xử lý upload ảnh nếu có
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = "D:/FlowerImg/";
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

                Path filePath = Paths.get(uploadDir + uniqueFilename);
                Files.write(filePath, imageFile.getBytes());

                // Cập nhật đường dẫn ảnh trong entity
                productEntity.setImagePath(uniqueFilename);

            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
                List<CategoryEntity> cateList = categoryService.findAllCategories();
                model.addAttribute("productEntity", productEntity);
                model.addAttribute("categories", cateList);
                model.addAttribute("suppliers", supplierService.getAll());
                return "product/editProduct";
            }
        }

        boolean updated = productService.updateProd(productEntity.getId(), productEntity);
        if (updated) {
            redirectAttributes.addFlashAttribute("message", "Product updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update product");
        }
        return "redirect:/products/ListProducts";
    }
}