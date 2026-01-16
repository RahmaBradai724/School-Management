package com.example.SpringProject.config;

import com.example.SpringProject.entity.*;
import com.example.SpringProject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdministratorRepository administratorRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (administratorRepository.count() > 0) {
            return;
        }

        // Create Administrator
        Administrator admin = new Administrator();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@school.com");
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setRole(UserRole.ADMIN);
        admin.setAdminId("ADM001");
        admin.setDepartment("IT");
        administratorRepository.save(admin);

        // Create Teachers
        Teacher teacher1 = new Teacher();
        teacher1.setUsername("jdoe");
        teacher1.setPassword(passwordEncoder.encode("teacher123"));
        teacher1.setEmail("jdoe@school.com");
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");
        teacher1.setRole(UserRole.TEACHER);
        teacher1.setTeacherId("TCH001");
        teacher1.setSpecialization("Computer Science");
        teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setUsername("asmith");
        teacher2.setPassword(passwordEncoder.encode("teacher123"));
        teacher2.setEmail("asmith@school.com");
        teacher2.setFirstName("Alice");
        teacher2.setLastName("Smith");
        teacher2.setRole(UserRole.TEACHER);
        teacher2.setTeacherId("TCH002");
        teacher2.setSpecialization("Mathematics");
        teacherRepository.save(teacher2);

        // Create Students
        Student student1 = new Student();
        student1.setUsername("mbrown");
        student1.setPassword(passwordEncoder.encode("student123"));
        student1.setEmail("mbrown@school.com");
        student1.setFirstName("Michael");
        student1.setLastName("Brown");
        student1.setRole(UserRole.STUDENT);
        student1.setStudentId("STU001");
        student1.setEnrollmentDate(LocalDate.of(2024, 9, 1));
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setUsername("sjohnson");
        student2.setPassword(passwordEncoder.encode("student123"));
        student2.setEmail("sjohnson@school.com");
        student2.setFirstName("Sarah");
        student2.setLastName("Johnson");
        student2.setRole(UserRole.STUDENT);
        student2.setStudentId("STU002");
        student2.setEnrollmentDate(LocalDate.of(2024, 9, 1));
        studentRepository.save(student2);

        Student student3 = new Student();
        student3.setUsername("dwilliams");
        student3.setPassword(passwordEncoder.encode("student123"));
        student3.setEmail("dwilliams@school.com");
        student3.setFirstName("David");
        student3.setLastName("Williams");
        student3.setRole(UserRole.STUDENT);
        student3.setStudentId("STU003");
        student3.setEnrollmentDate(LocalDate.of(2024, 9, 1));
        studentRepository.save(student3);

        // Create Courses
        Course course1 = new Course();
        course1.setCourseCode("CS101");
        course1.setCourseName("Introduction to Programming");
        course1.setDescription("Learn the basics of programming using Java");
        course1.setCredits(3);
        course1.setTeacher(teacher1);
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setCourseCode("CS201");
        course2.setCourseName("Data Structures");
        course2.setDescription("Advanced data structures and algorithms");
        course2.setCredits(4);
        course2.setTeacher(teacher1);
        courseRepository.save(course2);

        Course course3 = new Course();
        course3.setCourseCode("MATH101");
        course3.setCourseName("Calculus I");
        course3.setDescription("Differential and integral calculus");
        course3.setCredits(4);
        course3.setTeacher(teacher2);
        courseRepository.save(course3);

        Course course4 = new Course();
        course4.setCourseCode("MATH201");
        course4.setCourseName("Linear Algebra");
        course4.setDescription("Vector spaces and linear transformations");
        course4.setCredits(3);
        course4.setTeacher(teacher2);
        courseRepository.save(course4);

        // Create Enrollments
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudent(student1);
        enrollment1.setCourse(course1);
        enrollment1.setEnrollmentDate(LocalDate.of(2024, 9, 5));
        enrollment1.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(enrollment1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student1);
        enrollment2.setCourse(course3);
        enrollment2.setEnrollmentDate(LocalDate.of(2024, 9, 5));
        enrollment2.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(enrollment2);

        Enrollment enrollment3 = new Enrollment();
        enrollment3.setStudent(student2);
        enrollment3.setCourse(course1);
        enrollment3.setEnrollmentDate(LocalDate.of(2024, 9, 6));
        enrollment3.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(enrollment3);

        Enrollment enrollment4 = new Enrollment();
        enrollment4.setStudent(student2);
        enrollment4.setCourse(course2);
        enrollment4.setEnrollmentDate(LocalDate.of(2024, 9, 6));
        enrollment4.setStatus(EnrollmentStatus.ACTIVE);
        enrollmentRepository.save(enrollment4);

        // Create Grades
        Grade grade1 = new Grade();
        grade1.setEnrollment(enrollment1);
        grade1.setExamType("Midterm");
        grade1.setScore(85.5);
        grade1.setDateRecorded(LocalDate.of(2024, 10, 15));
        grade1.setComments("Good work");
        gradeRepository.save(grade1);

        Grade grade2 = new Grade();
        grade2.setEnrollment(enrollment1);
        grade2.setExamType("Final");
        grade2.setScore(92.0);
        grade2.setDateRecorded(LocalDate.of(2024, 12, 15));
        grade2.setComments("Excellent performance");
        gradeRepository.save(grade2);

        Grade grade3 = new Grade();
        grade3.setEnrollment(enrollment2);
        grade3.setExamType("Midterm");
        grade3.setScore(78.0);
        grade3.setDateRecorded(LocalDate.of(2024, 10, 20));
        grade3.setComments("Needs improvement");
        gradeRepository.save(grade3);

        System.out.println("========================================");
        System.out.println("Sample data initialized successfully!");
        System.out.println("========================================");
        System.out.println("Admin credentials: admin / admin123");
        System.out.println("Teacher credentials: jdoe / teacher123");
        System.out.println("Student credentials: mbrown / student123");
        System.out.println("========================================");
    }
}
