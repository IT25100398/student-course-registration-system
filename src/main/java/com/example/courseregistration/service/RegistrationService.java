package com.example.courseregistration.service;

import com.example.courseregistration.dto.RegistrationDTO;
import com.example.courseregistration.model.Course;
import com.example.courseregistration.model.Registration;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.repository.CourseRepository;
import com.example.courseregistration.repository.RegistrationRepository;
import com.example.courseregistration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public List<RegistrationDTO> getAllRegistrations() {
        return registrationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RegistrationDTO getRegistrationById(Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found with id: " + id));
        return convertToDTO(registration);
    }

    public Registration getRegistrationEntityById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found with id: " + id));
    }

    @Transactional
    public RegistrationDTO registerCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (registrationRepository.findByStudentAndCourse(student, course).isPresent()) {
            throw new RuntimeException("Student already registered for this course");
        }

        if (!course.isAvailable()) {
            throw new RuntimeException("Course is full");
        }

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setCourse(course);
        registration.setRegistrationDate(LocalDate.now());
        registration.setStatus("ENROLLED");

        course.setEnrolledCount(course.getEnrolledCount() + 1);
        courseRepository.save(course);

        registration = registrationRepository.save(registration);
        return convertToDTO(registration);
    }

    @Transactional
    public RegistrationDTO updateRegistration(Long id, RegistrationDTO registrationDTO) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        // Update grade
        if (registrationDTO.getGrade() != null) {
            registration.setGrade(registrationDTO.getGrade());
        }

        // Update status
        if (registrationDTO.getStatus() != null) {
            String oldStatus = registration.getStatus();
            String newStatus = registrationDTO.getStatus();

            registration.setStatus(newStatus);

            // Update course enrolled count if status changed from ENROLLED to DROPPED
            if (oldStatus.equals("ENROLLED") && newStatus.equals("DROPPED")) {
                Course course = registration.getCourse();
                course.setEnrolledCount(course.getEnrolledCount() - 1);
                courseRepository.save(course);
            }

            // Update course enrolled count if status changed from DROPPED to ENROLLED
            if (oldStatus.equals("DROPPED") && newStatus.equals("ENROLLED")) {
                Course course = registration.getCourse();
                if (course.isAvailable()) {
                    course.setEnrolledCount(course.getEnrolledCount() + 1);
                    courseRepository.save(course);
                } else {
                    throw new RuntimeException("Cannot re-enroll - Course is full");
                }
            }
        }

        registration = registrationRepository.save(registration);
        return convertToDTO(registration);
    }

    @Transactional
    public void dropCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Registration registration = registrationRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        course.setEnrolledCount(course.getEnrolledCount() - 1);
        courseRepository.save(course);

        registrationRepository.delete(registration);
    }

    @Transactional
    public void deleteRegistration(Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        // If status is ENROLLED, decrease course count
        if (registration.getStatus().equals("ENROLLED")) {
            Course course = registration.getCourse();
            course.setEnrolledCount(course.getEnrolledCount() - 1);
            courseRepository.save(course);
        }

        registrationRepository.delete(registration);
    }

    public List<RegistrationDTO> getRegistrationsByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return registrationRepository.findByStudent(student).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RegistrationDTO convertToDTO(Registration registration) {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setId(registration.getId());
        dto.setStudentId(registration.getStudent().getId());
        dto.setStudentName(registration.getStudent().getFullName());
        dto.setCourseId(registration.getCourse().getId());
        dto.setCourseName(registration.getCourse().getCourseName());
        dto.setCourseCode(registration.getCourse().getCourseCode());
        dto.setRegistrationDate(registration.getRegistrationDate());
        dto.setGrade(registration.getGrade());
        dto.setStatus(registration.getStatus());
        return dto;
    }

    @Transactional
    public void assignGrade(Long registrationId, String grade) {
        Registration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        reg.setGrade(grade);
        registrationRepository.save(reg);
    }


}