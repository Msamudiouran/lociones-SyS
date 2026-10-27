package com.locionesaaa.natillera.repository;
import com.locionesaaa.natillera.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface LoanRepository extends JpaRepository<Loan, Long> { List<Loan> findByMembershipId(Long membershipId); }
