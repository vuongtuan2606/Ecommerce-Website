package com.qtsneaker.ShopFrontEnd.controller;


import com.qtsneaker.ShopFrontEnd.dao.ProvinceRepository;
import com.qtsneaker.ShopFrontEnd.services.AddressService;
import com.qtsneaker.ShopFrontEnd.services.CustomerService;
import com.qtsneaker.common.entity.Address;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Province;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class AddressController {

	@Autowired private AddressService addressService;
	@Autowired private CustomerService customerService;
	@Autowired private ControllerHelper controllerHelper;

	@Autowired private ProvinceRepository provinceRepository;
	
	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		// Lấy thông tin khách hàng trong phiên đăng nhập
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);

		// Lấy danh sách địa chỉ của khách hàng tương ứng
		List<Address> listAddresses = addressService.listAddressBook(customer);
		
		boolean usePrimaryAddressAsDefault = true;

		for (Address address : listAddresses) {
			if (address.isDefaultForShipping()) {

				usePrimaryAddressAsDefault = false;

				break;
			}
		}
		
		model.addAttribute("listAddress", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		
		return "customer/account_detail_2";
	}
	
	@GetMapping("/address_book/new")
	public String newAddress(Model model) {
		List<Province> listProvince = provinceRepository.findAllByOrderByNameAsc();
		
		model.addAttribute("listProvince", listProvince);
		model.addAttribute("address", new Address());
		model.addAttribute("pageTitle", "Add New Address");
		
		return "address_book/address_form";
	}
	
	@PostMapping("/address_book/save")
	public String saveAddress(Address address,
							  HttpServletRequest request,
							  RedirectAttributes ra) {

		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		
		address.setCustomer(customer);
		addressService.save(address);
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		if ("checkout".equals(redirectOption)) {
			redirectURL += "?redirect=checkout";
		}
		
		ra.addFlashAttribute("message", "Địa chỉ của bạn đã được lưu thành công !.");
		
		return redirectURL;
	}
	
	@GetMapping("/address_book/edit/{id}")
	public String editAddress(@PathVariable("id") Integer addressId,
							  Model model,
							  HttpServletRequest request) {

		Customer customer = controllerHelper.getAuthenticatedCustomer(request);

		List<Province> listProvince = provinceRepository.findAllByOrderByNameAsc();
		
		Address address = addressService.get(addressId, customer.getId());

		model.addAttribute("address", address);
		model.addAttribute("listProvince", listProvince);
		model.addAttribute("pageTitle", "Edit Address (ID: " + addressId + ")");
		
		return "address_book/address_form";
	}
	
	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable("id") Integer addressId,
								RedirectAttributes ra,
								HttpServletRequest request) {

		Customer customer = controllerHelper.getAuthenticatedCustomer(request);

		addressService.delete(addressId, customer.getId());
		
		ra.addFlashAttribute("message", "The address ID " + addressId + " has been deleted.");
		
		return "redirect:/address_book";
	}
	
	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress(@PathVariable("id") Integer addressId,
									HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		addressService.setDefaultAddress(addressId, customer.getId());
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		if ("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		} else if ("checkout".equals(redirectOption)) {
			redirectURL = "redirect:/checkout";
		}
		
		return redirectURL; 
	}
}
