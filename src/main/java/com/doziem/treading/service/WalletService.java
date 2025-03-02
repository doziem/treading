package com.doziem.treading.service;

import com.doziem.treading.domain.OrderType;
import com.doziem.treading.model.Order;
import com.doziem.treading.model.Users;
import com.doziem.treading.model.Wallet;
import com.doziem.treading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
public class WalletService implements IWalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(Users user) {

        Wallet wallet = walletRepository.findUserByUserId(user.getId());
        if(wallet== null){
            wallet = new Wallet();
            wallet.setUser(user);

        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Double money) {
        BigDecimal balance = wallet.getBalance();

        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));

        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {

        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()) {
            return wallet.get();
        }
        throw new Exception("wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(Users sender, Wallet receiverWallet, Double money) throws Exception {
        Wallet senderWallet = getUserWallet(sender);

        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(money))<0){
            throw new Exception("Insufficient Balance....");
        }

        BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(money));
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        BigDecimal receiverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(money));
        receiverWallet.setBalance(receiverBalance);

        walletRepository.save(receiverWallet);

        return senderWallet;
    }

    @Override
    public Wallet payOrderWallet(Order order, Users user) throws Exception {
        Wallet wallet = getUserWallet(user);

        BigDecimal newBalance;

        if(order.getOrderType().equals(OrderType.BUY)){
            newBalance = wallet.getBalance().subtract(order.getPrice());
            if (newBalance.compareTo(order.getPrice())<0){
                throw new Exception("Insufficient Balance....");
            }
            wallet.setBalance(newBalance);
        }else {
            newBalance = wallet.getBalance().add(order.getPrice());
        }
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }
}
