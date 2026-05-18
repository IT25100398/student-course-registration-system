package com.example.courseregistration.controller;

import com.example.courseregistration.service.CourseService;
import com.example.courseregistration.service.RegistrationService;
import com.example.courseregistration.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final RegistrationService registrationService;

    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    @GetMapping("/landing")
    public String landingPage() {
        return "landing";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", studentService.getAllStudents().size());
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        model.addAttribute("totalRegistrations", registrationService.getAllRegistrations().size());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("recentRegistrations", registrationService.getAllRegistrations());
        return "dashboard";
    }
}