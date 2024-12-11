package com.doziem.treading.service;

import com.doziem.treading.model.Order;
import com.doziem.treading.model.Users;
import com.doziem.treading.model.Wallet;

public interface IWalletService {

    Wallet getUserWallet(Users user);
    Wallet addBalance(Wallet wallet,Double money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(Users sender,Wallet receiverWallet, Double money) throws Exception;
    Wallet payOrderWallet(Order order, Users user) throws Exception;


}
