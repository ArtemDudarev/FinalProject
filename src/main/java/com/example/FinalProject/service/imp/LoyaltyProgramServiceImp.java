package com.example.FinalProject.service.imp;

import com.example.FinalProject.dto.LoyaltyProgramDto;
import com.example.FinalProject.mapper.LoyaltyProgramMapper;
import com.example.FinalProject.model.LoyaltyProgram;
import com.example.FinalProject.repository.LoyaltyProgramRepository;
import com.example.FinalProject.service.LoyaltyProgramService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoyaltyProgramServiceImp implements LoyaltyProgramService {

    private static final Logger log = LoggerFactory.getLogger(LoyaltyProgramServiceImp.class);
    private final LoyaltyProgramRepository loyaltyProgramRepository;

    public LoyaltyProgramServiceImp(LoyaltyProgramRepository loyaltyProgramRepository) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
    }

    @Transactional
    public LoyaltyProgramDto createLoyaltyProgram(LoyaltyProgramDto loyaltyProgramDto) {
        try {
            LoyaltyProgram loyaltyProgram = LoyaltyProgramMapper.toEntity(loyaltyProgramDto);
            loyaltyProgramRepository.save(loyaltyProgram);
            log.info(String.format("Программа лояльности %s успешно сохранена", loyaltyProgram.getProgramName()));
            return loyaltyProgramDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s программы лояльности", loyaltyProgramDto.getProgramName()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s программы лояльности", loyaltyProgramDto.getProgramName()), e);
        }
    }

    @Transactional
    public void updateLoyaltyProgram(UUID loyaltyProgramId, LoyaltyProgramDto loyaltyProgramDto) {
        try {
            if (loyaltyProgramRepository.existsById(loyaltyProgramId)) {
                LoyaltyProgram loyaltyProgram = LoyaltyProgramMapper.toEntity(loyaltyProgramDto);
                loyaltyProgram.setLoyaltyProgramId(loyaltyProgramId);
                loyaltyProgramRepository.save(loyaltyProgram);
                log.info(String.format("Программа лояльности %s обновлена", loyaltyProgram.getProgramName()));
            } else {
                log.info("Программа лояльности не найдена");
                throw new EntityNotFoundException("Программа лояльности не найдена");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s программы лояльности", loyaltyProgramDto.getProgramName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s программы лояльности", loyaltyProgramDto.getProgramName()), e);
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
    public LoyaltyProgramDto findLoyaltyProgramDtoById(UUID loyaltyProgramId) {
        try {
            log.info(String.format("Получение программы лояльности Dto по ID: %s", loyaltyProgramId));
            return LoyaltyProgramMapper.toDto(loyaltyProgramRepository.findById(loyaltyProgramId).orElseThrow(() -> new EntityNotFoundException("Программа лояльности не найдена")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении программы лояльности Dto по ID: %s", loyaltyProgramId), e);
            throw new RuntimeException(String.format("Ошибка при получении программы лояльности Dto по ID: %s", loyaltyProgramId), e);
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

    @Transactional(readOnly = true)
    public List<LoyaltyProgramDto> findAllDto() {
        try {
            log.info("Получение всех программ лояльности Dto");
            List<LoyaltyProgram> loyaltyPrograms = loyaltyProgramRepository.findAll();
            return loyaltyPrograms.stream().map(LoyaltyProgramMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех программ лояльности Dto", e);
            throw new RuntimeException("Ошибка при получении всех программ лояльности Dto", e);
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

