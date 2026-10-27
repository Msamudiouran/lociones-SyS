package com.locionesaaa.natillera.repository;
import com.locionesaaa.natillera.entity.Liquidation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface LiquidationRepository extends JpaRepository<Liquidation, Long> { Optional<Liquidation> findByCycleId(Long cycleId); }
