package mng.qlkt.repository;

import mng.qlkt.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name); //Tim kiem User co ton tai trong db khong

    Boolean existsByUsername(String name); //Kiem tra username co ton tai tong db
    Boolean existsByEmail(String email); //Kiem tra email co ton tai trong db
    Optional<User> findById(Long id);

    @Query(value = "SELECT u FROM User u where u.isActive = 1" +
     "And (:name IS NULL OR u.name like %:name%) " +
            "And (:idUser IS NULL OR u.idUser like %:idUser%) "
    )

    Page<User> getAllUserList(Pageable pageable, String name, String idUser);

}
