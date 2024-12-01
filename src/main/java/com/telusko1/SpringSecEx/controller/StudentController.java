package com.telusko1.SpringSecEx.controller;

import com.telusko1.SpringSecEx.model.Student;
import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {


    private List<Student> students = new ArrayList<Student>(List.of(
                   new Student(1,"Navin",60),
                   new Student(2,"Kiran",65)

    ));

    @RequestMapping(path = "/students", method = RequestMethod.GET)
    public List<Student> getStudent(){
        return students;
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken)request.getAttribute("_csrf");
    }



   // @PostMapping(path = "/students",consumes = "application/json")
   @RequestMapping(path = "/students",method = RequestMethod.POST)
    public Student addStudent(@RequestBody Student student){
        System.out.println(student);
        students.add(student);
        return student;
    }

}
