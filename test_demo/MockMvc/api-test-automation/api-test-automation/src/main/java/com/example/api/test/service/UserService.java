package com.example.api.test.service;

import com.example.api.test.model.User;
import com.example.api.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Lấy tất cả users
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // Lấy user theo ID
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Lấy user theo email
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Tạo user mới
    public User createUser(User user) {
        // Validate business rules
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + user.getEmail());
        }

        // Additional validations
        validateUser(user);

        return userRepository.save(user);
    }

    // Cập nhật user
    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));

        // Check email conflict if email is being changed
        if (!existingUser.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + userDetails.getEmail());
        }

        // Validate new data
        validateUser(userDetails);

        // Update fields
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setAge(userDetails.getAge());
        existingUser.setAddress(userDetails.getAddress());
        existingUser.setPhone(userDetails.getPhone());

        return userRepository.save(existingUser);
    }

    // Xóa user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy user với id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Tìm kiếm users theo tên
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }
        return userRepository.findByNameContainingIgnoreCase(keyword.trim());
    }

    // Lấy users theo độ tuổi trong khoảng
    @Transactional(readOnly = true)
    public List<User> getUsersByAgeRange(Integer minAge, Integer maxAge) {
        if (minAge > maxAge) {
            throw new RuntimeException("Min age không được lớn hơn max age");
        }
        return userRepository.findByAgeBetween(minAge, maxAge);
    }

    // Lấy users theo tuổi cụ thể
    @Transactional(readOnly = true)
    public List<User> getUsersByAge(Integer age) {
        return userRepository.findByAge(age);
    }

    // Lấy users với phân trang
    @Transactional(readOnly = true)
    public Page<User> getUsersWithPagination(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    // Tìm kiếm với phân trang
    @Transactional(readOnly = true)
    public Page<User> searchUsersWithPagination(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return userRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    // Lấy users theo email domain
    @Transactional(readOnly = true)
    public List<User> getUsersByEmailDomain(String domain) {
        return userRepository.findByEmailDomain("%" + domain);
    }

    // Lấy users được tạo sau một thời điểm
    @Transactional(readOnly = true)
    public List<User> getUsersCreatedAfter(LocalDateTime dateTime) {
        return userRepository.findUsersCreatedAfter(dateTime);
    }

    // Thống kê users theo tuổi
    @Transactional(readOnly = true)
    public List<Object[]> getUserAgeStatistics() {
        return userRepository.countUsersByAge();
    }

    // Kiểm tra email có tồn tại không
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Đếm tổng số users
    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    // Validate user data
    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new RuntimeException("Tên không được để trống");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email không được để trống");
        }

        if (user.getAge() == null || user.getAge() < 18 || user.getAge() > 120) {
            throw new RuntimeException("Tuổi phải từ 18 đến 120");
        }

        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!user.getEmail().matches(emailRegex)) {
            throw new RuntimeException("Email không hợp lệ");
        }

        // Validate phone if provided
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            String phoneRegex = "^(\\+84|0)\\d{9,10}$";
            if (!user.getPhone().matches(phoneRegex)) {
                throw new RuntimeException("Số điện thoại không hợp lệ");
            }
        }
    }
}