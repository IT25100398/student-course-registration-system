package com.example.courseregistration.controller;

import com.example.courseregistration.dto.StudentDTO;
import com.example.courseregistration.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students/list";
    }

    // Filter Endpoint
    @GetMapping("/filter")
    @ResponseBody
    public List<StudentDTO> filterStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dateRange) {
        return studentService.filterStudents(search, status, dateRange);
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "students/add";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute StudentDTO studentDTO, RedirectAttributes redirectAttributes) {
        try {
            studentService.createStudent(studentDTO);
            redirectAttributes.addFlashAttribute("success", "Student added successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "students/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute StudentDTO studentDTO, RedirectAttributes redirectAttributes) {
        try {
            studentService.updateStudent(id, studentDTO);
            redirectAttributes.addFlashAttribute("success", "Student updated successfully!");
            return "redirect:/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/students";
    }
}