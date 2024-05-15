package com.qtsneaker.ShopBackEnd.controller;
import com.qtsneaker.ShopBackEnd.dao.AdminCustomerRepository;
import com.qtsneaker.ShopBackEnd.dao.AdminOrderRepository;
import org.apache.poi.ooxml.POIXMLProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class AdminHomeController {
    @Autowired
    private AdminCustomerRepository customerRepository;
    @Autowired
    private AdminOrderRepository orderRepository;
    @GetMapping("/")
    public String HomeAdmin(Model model){
        Date today = new Date();


        Long totalCustomersThisYear = customerRepository.countCustomersByYear(today);


        Long totalOrderToday = orderRepository.countOrderByDay(today);
        Long totalOrderMonth= orderRepository.countOrderByMonth(today);
        Long totalOrderYear = orderRepository.countOrderByYear(today);


        model.addAttribute("totalCustomersThisYear", totalCustomersThisYear);

        model.addAttribute("totalOrderToday",totalOrderToday);
        model.addAttribute("totalOrderMonth",totalOrderMonth);
        model.addAttribute("totalOrderYear",totalOrderYear);

        return "/index";
    }

}
