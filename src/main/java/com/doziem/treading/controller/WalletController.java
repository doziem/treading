package com.doziem.treading.controller;


import com.doziem.treading.model.Order;
import com.doziem.treading.model.Users;
import com.doziem.treading.model.Wallet;
import com.doziem.treading.model.WalletTransaction;
import com.doziem.treading.service.IOrderService;
import com.doziem.treading.service.IUserService;
import com.doziem.treading.service.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private IWalletService walletService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @GetMapping("/user-wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        Users user = userService.findUserByProfileJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction req
            ) throws Exception {

        Users senderUser = userService.findUserByProfileJwt(jwt);

        Wallet receicerWallet = walletService.findWalletById(walletId);

        Wallet wallet = walletService.walletToWalletTransfer(senderUser,receicerWallet,req.getAmount());

        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderWallet(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId,
            @RequestBody WalletTransaction req
    ) throws Exception {

        Users user = userService.findUserByProfileJwt(jwt);

        Order order = orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderWallet(order,user);

        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }

}
