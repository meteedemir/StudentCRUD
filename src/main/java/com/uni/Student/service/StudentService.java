package com.uni.Student.service;

import com.uni.Student.dto.StudentDTO;
import com.uni.Student.model.Student;
import com.uni.Student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final MongoTemplate mongoTemplate;

    public Page<Student> findAll(StudentDTO studentDTO, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        if (studentDTO.getName() != null) {
            criteria.add(Criteria.where("name").is(studentDTO.getName()));
        }
        if (studentDTO.getEmail() != null) {
            criteria.add(Criteria.where("email").is(studentDTO.getEmail()));

        }
        if (studentDTO.getCourseName() != null) {
            criteria.add(Criteria.where("courseName").is(studentDTO.getCourseName()));

        }
        if (studentDTO.getEcts() != null) {
            criteria.add(Criteria.where("ects").is(studentDTO.getEcts()));

        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        List<Student> students = mongoTemplate.find(query, Student.class);

        return PageableExecutionUtils.getPage(
                students, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Student.class)
        );

    }

    public Optional<Student> findById(String id) {
        return studentRepository.findById(id);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void deleteById(String id) {
        studentRepository.deleteById(id);
    }
}