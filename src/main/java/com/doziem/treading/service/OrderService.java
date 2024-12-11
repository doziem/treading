package com.doziem.treading.service;

import com.doziem.treading.domain.OrderStatus;
import com.doziem.treading.domain.OrderType;
import com.doziem.treading.model.Coin;
import com.doziem.treading.model.Order;
import com.doziem.treading.model.OrderItem;
import com.doziem.treading.model.Users;
import com.doziem.treading.repository.OrderItemRepository;
import com.doziem.treading.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService implements IOrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private IWalletService walletService;

    @Override
    public Order createOrder(Users user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();

        Order order = new Order();

        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setTimestamp(LocalDate.now());
        order.setPrice(BigDecimal.valueOf(price));
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {

        return orderRepository.findById(orderId).orElseThrow(()->new Exception("Order not found"));

    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return List.of();
    }

    private OrderItem createOrderItem(Coin coin,double quantity, double buyPrice, double sellPrice){
        OrderItem orderItem = new OrderItem();

        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setSellPrice(sellPrice);
        orderItem.setBuyPrice(buyPrice);

        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin,double quantity,Users user) throws Exception {

        if(quantity<0){
            throw new Exception("Quantity should be greater than 0");
        }
        double buyPrice = coin.getCurrentPrice();

        OrderItem orderItem = createOrderItem(coin,quantity,buyPrice,0);

        Order order = createOrder(user,orderItem,OrderType.BUY);

        orderItem.setOrder(order);

        walletService.payOrderWallet(order,user);

        order.setStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);

        return orderRepository.save(order);

    }

    @Override
    public Order processOrder(Coin coin, double quantity, OrderType orderType, Users user) {
        return null;
    }
}
