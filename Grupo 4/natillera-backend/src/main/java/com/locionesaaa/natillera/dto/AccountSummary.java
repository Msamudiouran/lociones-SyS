package com.locionesaaa.natillera.dto;
import java.math.BigDecimal;
public record AccountSummary(Long memberId, String memberName, BigDecimal contributions, BigDecimal approvedLoans, BigDecimal interestToPay, BigDecimal loanPayments, BigDecimal pendingLoanBalance, BigDecimal liquidationShare) {}
