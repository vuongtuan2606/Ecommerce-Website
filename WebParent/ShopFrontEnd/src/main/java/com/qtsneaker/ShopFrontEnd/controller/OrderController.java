package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.services.Order.OrderService;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.order.Order;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class OrderController {
	@Autowired private OrderService orderService;
	@Autowired private ControllerHelper controllerHelper;

	
	@GetMapping("/orders")
	public String listFirstPage(Model model, HttpServletRequest request) {
		return listOrdersByPage(model, request, 1, "orderTime", "desc", null);
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listOrdersByPage(Model model,
								   HttpServletRequest request,
								   @PathVariable(name = "pageNum") int pageNum,
								   @Param("sortField")String sortField,
								   @Param("sortDir") String sortDir,
								   @Param("keyword") String keyword
			) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		
		Page<Order> page = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, keyword);
		List<Order> listOrders = page.getContent();
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("moduleURL", "/orders");
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		model.addAttribute("startCount", startCount);
		
		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("endCount", endCount);
		
		return "customer/order_customer";
	}

	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(Model model,
			@PathVariable(name = "id") Integer id, HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		Order order = orderService.getOrder(id, customer);
		
		model.addAttribute("order", order);
		model.addAttribute("pageTitle","Chi tiết đơn hàng");
		
		return "customer/order_detail";
	}	

}
