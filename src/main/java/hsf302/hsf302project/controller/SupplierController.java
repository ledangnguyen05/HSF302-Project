package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.SupplierEntity;
import hsf302.hsf302project.repository.SupplierRepository;
import hsf302.hsf302project.service.SupplierService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("suppliers")
public class SupplierController {
    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SupplierService supplierService;

    @GetMapping("listSuppliers")
    public String supplierList(Model model){
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "supplier/supplierList";
    }

    @GetMapping("addPage")
    public String supplierCreatePage(Model model){
        model.addAttribute("supplier", new SupplierEntity());
        return "supplier/addSupplier";
    }

    @PostMapping("/addExecute")
    public String createExecute(@Valid @ModelAttribute("supplier") SupplierEntity supplierEntity,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            model.addAttribute("supplier",supplierEntity);
            return "supplier/addSupplier";
        }
        if(supplierRepository.findBySupplierNameIgnoreCase(supplierEntity.getSupplierName()).isPresent()){
            model.addAttribute("error","Supplier name is already token");
            return "supplier/addSupplier";
        }
        boolean isCreated=supplierService.addSupplier(supplierEntity);
        if(isCreated){
            redirectAttributes.addFlashAttribute("message","Supplier created successfully");
            return "redirect:/suppliers/listSuppliers";
        }else{
            model.addAttribute("error","Failed to add new supplier");
            return "supplier/addSupplier";
        }
    }

    @GetMapping("/updatePage/{id}")
    public String updatePage(@PathVariable int id, Model model){
        SupplierEntity supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID: " + id));
        model.addAttribute("supplier", supplier);
        return "supplier/updateSupplier";
    }


    @PostMapping("/updateExecute")
    public String updateExecute(@Valid @ModelAttribute("supplier") SupplierEntity supplierEntity,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            model.addAttribute("supplier",supplierEntity);
            return "supplier/updateSupplier";
        }
        supplierRepository.findBySupplierNameIgnoreCase(supplierEntity.getSupplierName())
                .ifPresent(existing -> {
                    if (existing.getId() != supplierEntity.getId()) {
                        model.addAttribute("error", "Supplier name is already taken");
                        throw new IllegalArgumentException();
                    }
                });
        boolean isUpdated=supplierService.update(supplierEntity.getId(),supplierEntity);
        if(isUpdated){
            redirectAttributes.addFlashAttribute("message","Supplier updated successfully");
            return "redirect:/suppliers/listSuppliers";
        }else{
            model.addAttribute("error","Failed to update supplier");
            return "supplier/updateSupplier";
        }
    }

    @GetMapping("deleteSupplier/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes){
        boolean isDeleted=supplierService.delete(id);
        if(isDeleted){
            redirectAttributes.addFlashAttribute("message","Supplier successfully deleted");
        }else{
            redirectAttributes.addFlashAttribute("error","Failed to delete supplier");
        }
        return "redirect:/suppliers/listSuppliers";
    }

    @PostMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyword,
                         @RequestParam(value = "searchType") String searchType,
                         Model model){
        List<SupplierEntity>suppliers=new ArrayList<>();
        switch (searchType){
            case "bySupplierName":
                suppliers=supplierRepository.findBySupplierNameContainingIgnoreCase(keyword.trim());
                break;
            case "byContactName":
                suppliers=supplierRepository.findByContactNameContainingIgnoreCase(keyword.trim());
                break;
            case "byPhone":
                suppliers=supplierRepository.findByPhone(keyword.trim());
                break;
        }
        if(suppliers.isEmpty()){
            model.addAttribute("error","Supplier not found for "+keyword.trim());
        }else{
            model.addAttribute("suppliers",suppliers);
        }
        model.addAttribute("searchType",searchType);
        return "supplier/supplierList";
    }

}
