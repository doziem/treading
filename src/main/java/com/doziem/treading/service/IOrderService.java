package com.doziem.treading.service;

import com.doziem.treading.domain.OrderType;
import com.doziem.treading.model.Coin;
import com.doziem.treading.model.Order;
import com.doziem.treading.model.OrderItem;
import com.doziem.treading.model.Users;

import java.util.List;

public interface IOrderService {

    Order createOrder(Users user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrdersOfUser(Long userId,OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, Users user);
}
