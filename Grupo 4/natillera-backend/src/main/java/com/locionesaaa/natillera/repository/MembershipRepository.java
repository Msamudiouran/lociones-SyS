package com.locionesaaa.natillera.repository;
import com.locionesaaa.natillera.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface MembershipRepository extends JpaRepository<Membership, Long> {
 Optional<Membership> findByCycleIdAndMemberId(Long cycleId, Long memberId);
 List<Membership> findByCycleId(Long cycleId);
}
