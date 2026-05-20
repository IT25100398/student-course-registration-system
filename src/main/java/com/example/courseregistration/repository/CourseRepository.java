package com.example.courseregistration.repository;

import com.example.courseregistration.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    boolean existsByCourseCode(String courseCode);

    // Semester/Year Filter Methods
    List<Course> findBySemester(String semester);
    List<Course> findByYear(int year);
    List<Course> findBySemesterAndYear(String semester, int year);
    List<Course> findByDepartmentAndSemesterAndYear(String department, String semester, int year);
}