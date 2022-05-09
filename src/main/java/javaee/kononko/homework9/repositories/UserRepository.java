package javaee.kononko.homework9.repositories;

import javaee.kononko.homework9.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT user FROM User user " + "LEFT JOIN FETCH user.permissions " + "WHERE user.login = :login")
    Optional<User> findByLogin(@Param("login") String login);
}
