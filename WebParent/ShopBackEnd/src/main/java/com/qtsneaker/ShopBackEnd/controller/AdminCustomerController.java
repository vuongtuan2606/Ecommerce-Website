package com.qtsneaker.ShopBackEnd.controller;

import com.qtsneaker.ShopBackEnd.services.AdminCustomerService;
import com.qtsneaker.common.entity.Customer;

import com.qtsneaker.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminCustomerController {
    @Autowired
    private AdminCustomerService service;
    @GetMapping("/customers")
    public String listFirstPage(Model model) {
        return listByPage(model, 1, "firstName", "asc", null);
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(Model model,
                             @PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword
    ) {

        Page<Customer> page = service.listByPage(pageNum, sortField, sortDir, keyword);

        List<Customer> listCustomers = page.getContent();

        long startCount = (pageNum - 1) * service.CUSTOMERS_PER_PAGE + 1;
        model.addAttribute("startCount", startCount);

        long endCount = startCount + service.CUSTOMERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);

        model.addAttribute("totalPages", page.getTotalPages());

        model.addAttribute("startCount", startCount);

        model.addAttribute("endCount", endCount);

        model.addAttribute("totalItem", page.getTotalElements());

        model.addAttribute("listCustomers", listCustomers);

        model.addAttribute("sortField", sortField);

        model.addAttribute("sortDir", sortDir);

        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("keyword", keyword);

        model.addAttribute("moduleURL", "/admin/customers");

        model.addAttribute("pageTitle","Danh sách khách hàng");

        return "/customer/customer";
    }
    @GetMapping("/customers/detail/{id}")
    public String viewDetailCustomer(@PathVariable("id") Integer id,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        try {
            Customer customer = service.get(id);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", "Thông tin khách hàng :(ID: " + id + ")");

            return "/customer/customer_detail";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/customers";
        }
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);

            redirectAttributes.addFlashAttribute("message", "Khách hàng có ID: " + id + " đã được xóa thành công!.");

        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/admin/customers";
    }
    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {

        service.updateCustomerEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";

        String message = "The Customer ID " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/customers";
    }
}
