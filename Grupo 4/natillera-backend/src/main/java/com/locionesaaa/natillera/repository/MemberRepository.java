package com.locionesaaa.natillera.repository;
import com.locionesaaa.natillera.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface MemberRepository extends JpaRepository<Member, Long> { Optional<Member> findByDocument(String document); }
