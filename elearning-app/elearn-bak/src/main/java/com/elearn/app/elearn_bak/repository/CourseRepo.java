package com.elearn.app.elearn_bak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elearn.app.elearn_bak.entities.Course;
import java.util.List;


@Repository
public interface CourseRepo extends JpaRepository<Course, String> {

    List<Course> findByTitle(String title);

    List<Course> findByLive(boolean live);

    // @Query("select c from Course c where c.categoryList.id")
    // List<Course> findByCategoryId(int id);

}
