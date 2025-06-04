package com.example.api.test.repository;

import com.example.api.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo email
    Optional<User> findByEmail(String email);

    // Kiểm tra email đã tồn tại
    boolean existsByEmail(String email);

    // Tìm users theo độ tuổi trong khoảng
    @Query("SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge ORDER BY u.age")
    List<User> findByAgeBetween(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    // Tìm kiếm user theo tên (không phân biệt hoa thường)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY u.name")
    List<User> findByNameContainingIgnoreCase(@Param("keyword") String keyword);

    // Tìm users theo tuổi cụ thể
    List<User> findByAge(Integer age);

    // Tìm users có tuổi lớn hơn
    List<User> findByAgeGreaterThan(Integer age);

    // Tìm users có tuổi nhỏ hơn
    List<User> findByAgeLessThan(Integer age);

    // Tìm users theo email domain
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain ORDER BY u.email")
    List<User> findByEmailDomain(@Param("domain") String domain);

    // Tìm users được tạo trong khoảng thời gian
    @Query("SELECT u FROM User u WHERE u.createdAt >= :startDate ORDER BY u.createdAt DESC")
    List<User> findUsersCreatedAfter(@Param("startDate") java.time.LocalDateTime startDate);

    // Phân trang và sắp xếp
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Thống kê số lượng users theo độ tuổi
    @Query("SELECT u.age, COUNT(u) FROM User u GROUP BY u.age ORDER BY u.age")
    List<Object[]> countUsersByAge();

    // Tìm user có ID lớn nhất
    @Query("SELECT u FROM User u WHERE u.id = (SELECT MAX(u2.id) FROM User u2)")
    Optional<User> findUserWithMaxId();

    // Custom delete query
    @Query("DELETE FROM User u WHERE u.age > :age")
    void deleteUsersOlderThan(@Param("age") Integer age);
}