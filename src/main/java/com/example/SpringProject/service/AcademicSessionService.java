package com.example.SpringProject.service;

import com.example.SpringProject.entity.AcademicSession;
import com.example.SpringProject.repository.AcademicSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicSessionService {

    private final AcademicSessionRepository academicSessionRepository;

    public List<AcademicSession> getAllSessions() {
        return academicSessionRepository.findAll();
    }

    public Optional<AcademicSession> getSessionById(Long id) {
        return academicSessionRepository.findById(id);
    }

    public Optional<AcademicSession> getActiveSession() {
        return academicSessionRepository.findByActiveTrue();
    }

    public AcademicSession createSession(AcademicSession session) {
        if (session.isActive()) {
            deactivateAllSessions();
        }
        return academicSessionRepository.save(session);
    }

    public AcademicSession updateSession(Long id, AcademicSession details) {
        AcademicSession session = academicSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (details.isActive() && !session.isActive()) {
            deactivateAllSessions();
        }

        session.setName(details.getName());
        session.setStartDate(details.getStartDate());
        session.setEndDate(details.getEndDate());
        session.setActive(details.isActive());

        return academicSessionRepository.save(session);
    }

    private void deactivateAllSessions() {
        academicSessionRepository.findAll().forEach(s -> {
            s.setActive(false);
            academicSessionRepository.save(s);
        });
    }

    public void deleteSession(Long id) {
        academicSessionRepository.deleteById(id);
    }
}
