package com.example.shopping.service;

import com.example.shopping.model.AppUser;
import com.example.shopping.model.AppUserRole;
import com.example.shopping.model.BankAccount;
import com.example.shopping.repo.AppUserRepo;
import com.example.shopping.repo.BankAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with login %s not found";
    private final AppUserRepo appUserRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BankAccountRepo bankAccountRepo;

    @Autowired
    public AppUserService(AppUserRepo appUserRepo,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          BankAccountRepo bankAccountRepo) {
        this.appUserRepo = appUserRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.bankAccountRepo = bankAccountRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepo.findByLogin(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }

    @Transactional
    public void registerNewUser(AppUser appUser) {
        if (appUserRepo.findByLogin(appUser.getLogin()).isPresent()) {
            throw new IllegalStateException("login already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setPassword(encodedPassword);
        appUserRepo.save(appUser);
        BankAccount bankAccount = new BankAccount(appUser.getAccountBalance(), appUser);
        bankAccountRepo.save(bankAccount);
    }


}
