package com.locionesaaa.natillera.repository;
import com.locionesaaa.natillera.entity.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Long> {
 @Query("select coalesce(sum(p.amount),0) from LoanPayment p where p.loan.id=:loanId")
 BigDecimal sumByLoan(Long loanId);
}
