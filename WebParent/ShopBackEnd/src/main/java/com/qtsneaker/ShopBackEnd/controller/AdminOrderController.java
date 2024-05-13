package com.qtsneaker.ShopBackEnd.controller;

import com.qtsneaker.ShopBackEnd.exception.OrderNotFoundException;
import com.qtsneaker.ShopBackEnd.security.AdminUserDetails;
import com.qtsneaker.ShopBackEnd.services.Order.AdminOrderService;
import com.qtsneaker.ShopBackEnd.services.Setting.AdminSettingService;
import com.qtsneaker.common.entity.order.Order;
import com.qtsneaker.common.entity.order.OrderStatus;
import com.qtsneaker.common.entity.order.OrderTrack;
import com.qtsneaker.common.entity.setting.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired private AdminOrderService service;

    @Autowired private AdminSettingService settingService;
    @GetMapping("/orders")
    public String listFirstPage(Model model,HttpServletRequest request) {
        loadCurrencySetting(request);
        return listByPage( "orderTime", "desc", null
                ,1,model);
    }
    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @PathVariable(name = "pageNum") int pageNum,
                             Model model
                            ) {

        Page<Order> page = service.listByPage(pageNum, sortField, sortDir, keyword);
        List<Order> listOrder = page.getContent();


        long startCount = (pageNum - 1) * AdminOrderService.ORDERS_PER_PAGE + 1;

        long endCount = startCount + AdminOrderService.ORDERS_PER_PAGE - 1;

        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);

        model.addAttribute("totalPages", page.getTotalPages());

        model.addAttribute("totalItem", page.getTotalElements());

        model.addAttribute("startCount", startCount);

        model.addAttribute("endCount", endCount);

        model.addAttribute("sortField", sortField);

        model.addAttribute("sortDir", sortDir);

        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("keyword", keyword);

        model.addAttribute("moduleURL", "/admin/orders");

        model.addAttribute("listOrder", listOrder);

        model.addAttribute("pageTitle", "Danh sách đơn hàng");

        return "order/orders";
    }
    private void loadCurrencySetting(HttpServletRequest request) {

        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings) {

            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }
    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id,
                                   Model model,
                                   RedirectAttributes ra,
                                   HttpServletRequest request,
                                   @AuthenticationPrincipal AdminUserDetails loggedUse) {
        try {
            Order order = service.get(id);

            loadCurrencySetting(request);
            model.addAttribute("order", order);
            model.addAttribute("pageTitle","Chi tiết đơn hàng");

            return "order/order_detail_form";

        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/orders";
        }
    }
    @GetMapping("/orders/status/{id}")
    public String viewOrderStatus(@PathVariable("id") Integer id,
                                   Model model,
                                   RedirectAttributes ra,
                                   HttpServletRequest request,
                                   @AuthenticationPrincipal AdminUserDetails loggedUse) {
        try {
            Order order = service.get(id);

            loadCurrencySetting(request);
            model.addAttribute("order", order);
            model.addAttribute("pageTitle","Cập nhật trạng thái đơn hàng ID:" +id );

            return "order/order_update_status";

        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/admin/orders";
        }
    }
}


