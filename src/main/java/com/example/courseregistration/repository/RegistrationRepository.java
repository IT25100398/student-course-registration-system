package com.example.courseregistration.repository;

import com.example.courseregistration.model.Registration;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByStudent(Student student);
    List<Registration> findByCourse(Course course);
    Optional<Registration> findByStudentAndCourse(Student student, Course course);
}