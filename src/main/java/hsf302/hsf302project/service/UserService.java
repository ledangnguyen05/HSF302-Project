package hsf302.hsf302project.service;

import hsf302.hsf302project.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity findByUsernameAndPassword(String username, String password);
    boolean registerUser(UserEntity user);
    boolean updateUser(int userId,UserEntity user);
    boolean deleteUser(int userId);
    List<UserEntity> findAllUsers();
    List<UserEntity> findByUsername(String username);
    List<UserEntity >findByEmail(String email);
    UserEntity findByUserId(Integer userId);
    UserEntity findByPhone(String phone);
}
