package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select count(u)>0 from User u where u.id = :id")
    boolean existsById(@Param("id") int id);
}
