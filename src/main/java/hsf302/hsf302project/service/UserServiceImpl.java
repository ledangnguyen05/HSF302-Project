package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.UserEntity;
import hsf302.hsf302project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserEntity findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public boolean registerUser(UserEntity user) {
        try{
            userRepository.save(user);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean updateUser(int userId, UserEntity user) {
        try{
            if(userRepository.existsById(userId)) {
                user.setId(userId);
                userRepository.save(user);
                return true;
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        try{
            userRepository.deleteById(userId);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserEntity> findByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Override
    public List<UserEntity> findByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email);
    }

    @Override
    public UserEntity findByUserId(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public UserEntity findByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }
}
