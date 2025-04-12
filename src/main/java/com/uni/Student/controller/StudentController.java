package com.uni.Student.controller;

import com.uni.Student.dto.StudentDTO;
import com.uni.Student.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uni.Student.service.StudentService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<Page<Student>> getAllStudents(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int size,
                                                        @RequestParam(defaultValue = "name") String sortBy,
                                                        @RequestParam(defaultValue = "ASC") String sortDir,
                                                        StudentDTO studentDTO){
        PageRequest pageRequest=PageRequest.of(page,size,
                Sort.Direction.valueOf(sortDir.toUpperCase()),sortBy);

        return ResponseEntity.ok(studentService.findAll(studentDTO,pageRequest));
    }


    @GetMapping("student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id){
        return studentService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("add/student")
    public ResponseEntity<Student> addStudent(@RequestBody Student student){
        return ResponseEntity.ok(studentService.save(student));

    }

     @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id,@RequestBody Student student){
        return studentService.findById(id)
                .map(existingStudent ->  {
                    student.setId(id);
                    return ResponseEntity.ok(studentService.save(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id){
        return studentService.findById(id)
                .map(student -> {
                    studentService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }


}
