package com.qtsneaker.ShopFrontEnd.services.Checkout;

import com.qtsneaker.common.entity.Cart;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Override
    public CheckoutInfo prepareCheckout(List<Cart> cartItems) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        // giá giảm * số lượng
        float productTotal = calculateProductTotal(cartItems);
        // giá nhập
        float productCost = calculateProductCost(cartItems);
        // tổng tiền
        float paymentTotal = productTotal ;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setPaymentTotal(paymentTotal);

        return checkoutInfo;
    }

    @Override
    public float calculateProductTotal(List<Cart> cartItems) {
        float total = 0.0f;

        for (Cart item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }
    @Override
    public float calculateProductCost(List<Cart> cartItems) {
        float cost = 0.0f;

        for (Cart item : cartItems) {
            cost += item.getQuantity() * item.getProduct().getCost();
        }

        return cost;
    }
}
