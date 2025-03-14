package com.example.FinalProject.service;

import com.example.FinalProject.model.LoyaltyProgram;
import com.example.FinalProject.repository.LoyaltyProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoyaltyProgramService {

    private static final Logger log = LoggerFactory.getLogger(LoyaltyProgramService.class);
    private final LoyaltyProgramRepository loyaltyProgramRepository;

    public LoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
    }

    @Transactional
    public void createLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        try {
            loyaltyProgramRepository.save(loyaltyProgram);
            log.info(String.format("Программа лояльности %s успешно сохранена", loyaltyProgram.getProgramName()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s программы лояльности", loyaltyProgram.getProgramName()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s программы лояльности", loyaltyProgram.getProgramName()), e);
        }
    }

    @Transactional
    public void updateLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        try {
            if (loyaltyProgramRepository.existsById(loyaltyProgram.getLoyaltyProgramId())) {
                loyaltyProgramRepository.save(loyaltyProgram);
                log.info(String.format("Программа лояльности %s обновлена", loyaltyProgram.getProgramName()));
            } else {
                log.info("Программа лояльности не найдена");
                throw new EntityNotFoundException("Программа лояльности не найдена");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s программы лояльности", loyaltyProgram.getProgramName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s программы лояльности", loyaltyProgram.getProgramName()), e);
        }
    }

    @Transactional(readOnly = true)
    public LoyaltyProgram findLoyaltyProgramById(UUID loyaltyProgramId) {
        try {
            log.info(String.format("Получение программы лояльности по ID: %s", loyaltyProgramId));
            return loyaltyProgramRepository.findById(loyaltyProgramId).orElseThrow(() -> new EntityNotFoundException("Программа лояльности не найдена"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении программы лояльности по ID: %s", loyaltyProgramId), e);
            throw new RuntimeException(String.format("Ошибка при получении программы лояльности по ID: %s", loyaltyProgramId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<LoyaltyProgram> findAll() {
        try {
            log.info("Получение всех программ лояльности");
            return loyaltyProgramRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех программ лояльности", e);
            throw new RuntimeException("Ошибка при получении всех программ лояльности", e);
        }
    }

    @Transactional
    public void deleteLoyaltyProgram(UUID loyaltyProgramId) {
        try {
            loyaltyProgramRepository.deleteById(loyaltyProgramId);
            log.info(String.format("Программа лояльности с ID: %s успешно удалена", loyaltyProgramId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении программы лояльности с ID: %s", loyaltyProgramId), e);
            throw new RuntimeException(String.format("Ошибка при удалении программы лояльности с ID: %s", loyaltyProgramId), e);
        }
    }
}

