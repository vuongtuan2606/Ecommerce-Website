package com.qtsneaker.ShopFrontEnd.rest;

import com.qtsneaker.ShopFrontEnd.controller.ControllerHelper;
import com.qtsneaker.ShopFrontEnd.dao.ProductRepository;
import com.qtsneaker.ShopFrontEnd.dao.ProvinceRepository;
import com.qtsneaker.ShopFrontEnd.exception.CartException;
import com.qtsneaker.ShopFrontEnd.services.Cart.CartService;
import com.qtsneaker.ShopFrontEnd.services.Customer.CustomerService;
import com.qtsneaker.ShopFrontEnd.util.Utility;
import com.qtsneaker.common.entity.Customer;
import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.exception.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartRestController {

	@Autowired private CartService cartService;

	@Autowired private CustomerService customerService;

	@Autowired private ProductRepository productRepository;


	@Autowired private ControllerHelper controllerHelper;


	/* Thêm sản phẩm vào giỏ hàng */
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
								   @PathVariable("quantity") Integer quantity,
								   @RequestParam("sizeId") Integer sizeId,
								   HttpServletRequest request) {

		try {
			// Lấy thông tin khách hàng đã xác thực từ request
			Customer customer = getAuthenticatedCustomer(request);

			//Gọi method addProducts để thêm sản phẩm vào giỏ hàng của khách hàng
			Integer updatedQuantity = cartService.addProducts(productId, quantity, customer,sizeId);

			// Trả về thông báo về số lượng sản phẩm được thêm vào giỏ hàng
			return updatedQuantity + " mặt hàng của sản phẩm này đã được thêm vào giỏ hàng của bạn.";

		} catch (CustomerNotFoundException ex) {

			return "Bạn phải đăng nhập để thêm sản phẩm này vào giỏ hàng.";

		} catch (CartException ex) {

			return ex.getMessage();
		}
	}


	/* Lấy thông tin của khách hàng đã xác thực từ HttpServletRequest.*/
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {

		// Lấy địa chỉ email của khách hàng đã xác thực từ Utility
		String email = Utility.getEmailOfAuthenticatedCustomer(request);

		// Kiểm tra xem có email của khách hàng đã xác thực chưa
		if (email == null) {

			// ném ra ngoại lệ
			throw new CustomerNotFoundException("Không có khách hàng được xác thực");
		}

		// Trả về thông tin của khách hàng dựa trên email
		return customerService.getCustomerByEmail(email);
	}

	/* Cập nhật số lượng của sản phẩm trong giỏ hàng.*/
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
								 @PathVariable("quantity") Integer quantity,
								 @RequestParam("sizeId") Integer sizeId,
								 HttpServletRequest request) throws CartException {
		try {
			// Lấy thông tin khách hàng đã xác thực từ request
			Customer customer = getAuthenticatedCustomer(request);

			//Gọi method updateQuantity để cập nhật số lượng của sản phẩm trong giỏ hàng
			float subtotal = cartService.updateQuantity(productId, quantity, customer,sizeId);

			// Trả về tổng cộng giá trị của sản phẩm sau khi cập nhật số lượng
			return String.valueOf(subtotal);

		} catch (CustomerNotFoundException ex) {
			// Trả về thông báo lỗi nếu không tìm thấy thông tin khách hàng đã xác thực
			return "Bạn phải đăng nhập để thay đổi số lượng sản phẩm.";
		}
	}

	@GetMapping("/cart/checkStock/{productId}/{quantity}")
	public ResponseEntity<String> checkStock(@PathVariable("productId") Integer productId,
											 @PathVariable("quantity") Integer quantity,
											 @RequestParam("sizeId") Integer sizeId) {
		Product product = productRepository.findById(productId).orElse(null);
		if (product == null) {
			return ResponseEntity.badRequest().body("Sản phẩm không tồn tại.");
		}

		int availableStock = product.getProductQuantity();

		if (quantity > availableStock) {
			return ResponseEntity.badRequest().body("Số lượng yêu cầu lớn hơn số lượng hiện có trong kho.");
		}

		return ResponseEntity.ok("Sản phẩm có sẵn.");
	}

	/* Xóa sản phẩm khỏi giỏ hàng*/
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,
			                    HttpServletRequest request) {
		try {
			// Lấy thông tin khách hàng đã xác thực từ request
			Customer customer = getAuthenticatedCustomer(request);

			// Gọi method removeProduct để xóa sản phẩm khổ giỏ hàng
			cartService.removeProduct(productId, customer);

			// Trả về thông báo xóa thành công
			return "Sản phẩm đã bị xóa khỏi giỏ hàng của bạn.";

		} catch (CustomerNotFoundException e) {

			// Trả về thông báo lỗi nếu không tìm thấy thông tin khách hàng đã xác thực
			return "Bạn cần đăng nhập để xóa sản phẩm.";
		}
	}
}
