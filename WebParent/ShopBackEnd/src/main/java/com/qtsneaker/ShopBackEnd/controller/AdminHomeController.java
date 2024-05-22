package com.qtsneaker.ShopBackEnd.controller;
import com.qtsneaker.ShopBackEnd.dao.AdminCustomerRepository;
import com.qtsneaker.ShopBackEnd.dao.AdminOrderDetailRepository;
import com.qtsneaker.ShopBackEnd.dao.AdminOrderRepository;
import com.qtsneaker.ShopBackEnd.services.Order.AdminOrderService;
import com.qtsneaker.ShopBackEnd.services.Setting.AdminSettingService;
import com.qtsneaker.common.entity.order.Order;
import com.qtsneaker.common.entity.order.OrderDetail;
import com.qtsneaker.common.entity.setting.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ooxml.POIXMLProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class AdminHomeController {
    @Autowired
    private AdminCustomerRepository customerRepository;
    @Autowired
    private AdminOrderRepository orderRepository;

    @Autowired
    private AdminOrderDetailRepository orderDetailRepository;
    @Autowired
    private AdminSettingService settingService;
    @GetMapping("/")
    public String HomeAdmin(Model model,HttpServletRequest request) throws ParseException {
        // format tiền tệ
        loadCurrencySetting(request);
        Pageable pageable = PageRequest.of(0, 5);

        Date today = new Date();
        Date startDates  = calculateStartDate(today, 7);
        Calendar calendar = Calendar.getInstance();

        // danh sách đơn hàng
        List<Order> listOrder = orderRepository.findTop6(pageable);

        // danh sách sản phẩm bán chạy
        List<OrderDetail> listOrderDetails = orderDetailRepository.findOrderDetailsByOrderTimeBetween(startDates, today, pageable);

        // Tính doanh thu hôm nay
        Long sumOrderToday = orderRepository.sumTotalToday(today);

        // Tính doanh thu tuần qua
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date startDate = calendar.getTime();
        Long sumOrderLast7Days = orderRepository.sumTotalLast7Days(startDate, endDate);

        // Tính doanh thu tháng qua
        calendar = Calendar.getInstance();
        endDate = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        startDate = calendar.getTime();
        Long sumOrderLastMonth = orderRepository.sumTotalLastMonth(startDate, endDate);

        // đếm số đơn đặt hàng
        Long totalCustomersThisYear = customerRepository.countCustomersByYear(today);
        Long totalOrderToday = orderRepository.countOrderByDay(today);
        Long totalOrderMonth= orderRepository.countOrderByMonth(today);
        Long totalOrderYear = orderRepository.countOrderByYear(today);

        model.addAttribute("totalCustomersThisYear", totalCustomersThisYear);
        model.addAttribute("totalOrderToday",totalOrderToday);
        model.addAttribute("totalOrderMonth",totalOrderMonth);
        model.addAttribute("totalOrderYear",totalOrderYear);

        model.addAttribute("sumOrderToday", sumOrderToday);
        model.addAttribute("sumOrderLast7Days", sumOrderLast7Days);
        model.addAttribute("sumOrderLastMonth", sumOrderLastMonth);

        model.addAttribute("listOrder",listOrder);
        model.addAttribute("listOrderDetails",listOrderDetails);

        return "/index";
    }
    private void loadCurrencySetting(HttpServletRequest request) {

        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings) {

            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }
    private Date calculateStartDate(Date endDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

}
