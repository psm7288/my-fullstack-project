// MemberRepository.java - Oracle 연동용
package com.example.pet.repository;

import com.example.pet.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원 찾기 (로그인용)
    Optional<Member> findByEmail(String email);

    // 이름으로 회원 찾기
    Optional<Member> findByName(String name);

    // 이메일과 비밀번호로 회원 찾기 (로그인 검증용)
    Optional<Member> findByEmailAndPassword(String email, String password);

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // 회원과 반려견 정보를 함께 조회
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.pets WHERE m.id = :memberId")
    Optional<Member> findMemberWithPets(@Param("memberId") Long memberId);
}