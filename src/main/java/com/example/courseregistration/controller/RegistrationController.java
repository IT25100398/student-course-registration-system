package com.example.courseregistration.controller;

import com.example.courseregistration.dto.RegistrationDTO;
import com.example.courseregistration.service.CourseService;
import com.example.courseregistration.service.RegistrationService;
import com.example.courseregistration.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping//Displaying Registration List
    public String listRegistrations(Model model) {
        model.addAttribute("registrations", registrationService.getAllRegistrations());
        return "registrations/list";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "registrations/register";
    }

    @PostMapping("/register")
    public String registerCourse(@RequestParam Long studentId, @RequestParam Long courseId, RedirectAttributes redirectAttributes) {
        try {
            registrationService.registerCourse(studentId, courseId);
            redirectAttributes.addFlashAttribute("success", "Student registered for course successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/registrations";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        RegistrationDTO registration = registrationService.getRegistrationById(id);
        model.addAttribute("registration", registration);
        return "registrations/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateRegistration(@PathVariable Long id, @ModelAttribute RegistrationDTO registrationDTO, RedirectAttributes redirectAttributes) {
        try {
            registrationService.updateRegistration(id, registrationDTO);
            redirectAttributes.addFlashAttribute("success", "Registration updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/registrations";
    }

    @GetMapping("/delete/{id}")
    public String deleteRegistration(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            registrationService.deleteRegistration(id);
            redirectAttributes.addFlashAttribute("success", "Registration deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/registrations";
    }


    @GetMapping("/drop/{studentId}/{courseId}")
    public String dropCourse(@PathVariable Long studentId, @PathVariable Long courseId, RedirectAttributes redirectAttributes) {
        try {
            registrationService.dropCourse(studentId, courseId);
            redirectAttributes.addFlashAttribute("success", "Course dropped successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/registrations";
    }
}