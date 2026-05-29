package com.airtribe.payflow.service;

import com.airtribe.payflow.entity.User;
import com.airtribe.payflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> findByUpiId(String upiId) {
        return userRepository.findByUpiId(upiId);
    }
    public List<User> findUsersWithBalanceAbove(Double minBalance){
        return userRepository.findUsersWithBalanceAbove(minBalance);
    }


}




























