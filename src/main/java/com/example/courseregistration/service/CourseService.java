package com.example.courseregistration.service;

import com.example.courseregistration.dto.CourseDTO;
import com.example.courseregistration.model.Course;
import com.example.courseregistration.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return convertToDTO(course);
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        if (courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            throw new RuntimeException("Course code already exists");
        }

        Course course = convertToEntity(courseDTO);
        course = courseRepository.save(course);
        return convertToDTO(course);
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setCourseCode(courseDTO.getCourseCode());
        course.setCourseName(courseDTO.getCourseName());
        course.setCredits(courseDTO.getCredits());
        course.setDepartment(courseDTO.getDepartment());
        course.setInstructor(courseDTO.getInstructor());
        course.setCapacity(courseDTO.getCapacity());
        course.setSchedule(courseDTO.getSchedule());
        course.setRoom(courseDTO.getRoom());
        course.setSemester(courseDTO.getSemester());
        course.setYear(courseDTO.getYear());
        course.setStatus(courseDTO.getStatus());

        course = courseRepository.save(course);
        return convertToDTO(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCourseCode(course.getCourseCode());
        dto.setCourseName(course.getCourseName());
        dto.setCredits(course.getCredits());
        dto.setDepartment(course.getDepartment());
        dto.setInstructor(course.getInstructor());
        dto.setCapacity(course.getCapacity());
        dto.setEnrolledCount(course.getEnrolledCount());
        dto.setSchedule(course.getSchedule());
        dto.setRoom(course.getRoom());
        dto.setSemester(course.getSemester());
        dto.setYear(course.getYear());
        dto.setStatus(course.getStatus());
        dto.setAvailable(course.isAvailable());
        dto.setAvailableSeats(course.getAvailableSeats());
        return dto;
    }

    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setCredits(dto.getCredits());
        course.setDepartment(dto.getDepartment());
        course.setInstructor(dto.getInstructor());
        course.setCapacity(dto.getCapacity());
        course.setSchedule(dto.getSchedule());
        course.setRoom(dto.getRoom());
        course.setSemester(dto.getSemester());
        course.setYear(dto.getYear());
        course.setStatus(dto.getStatus());
        return course;
    }
}