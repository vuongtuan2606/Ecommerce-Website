package com.qtsneaker.ShopFrontEnd.services.Checkout;

import com.qtsneaker.common.entity.Cart;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Override
    public CheckoutInfo prepareCheckout(List<Cart> cartItems) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productTotal = calculateProductTotal(cartItems);
        float shippingCostTotal = 30.0f;
        float paymentTotal = productTotal + shippingCostTotal;

        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setShippingCostTotal(shippingCostTotal);
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
}
