package com.locionesaaa.natillera.repository;
import com.locionesaaa.natillera.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
 @Query("select coalesce(sum(c.amount),0) from Contribution c where c.membership.id=:membershipId")
 BigDecimal sumByMembership(Long membershipId);
}
