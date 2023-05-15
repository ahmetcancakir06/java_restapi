package com.example.restapi2.Repo;
import com.example.restapi2.Models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PhotoRepo extends JpaRepository<Photo,Long> {
}
