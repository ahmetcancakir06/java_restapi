package com.example.restapi2.Repo;
import com.example.restapi2.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepo extends JpaRepository<User,Long> {
}
