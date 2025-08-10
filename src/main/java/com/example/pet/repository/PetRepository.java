// PetRepository.java - Oracle 연동용
package com.example.pet.repository;

import com.example.pet.domain.Pet;
import com.example.pet.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // 특정 회원의 반려견 목록 조회
    List<Pet> findByMember(Member member);

    // 회원 ID로 반려견 목록 조회
    List<Pet> findByMemberIdOrderByCreatedDateDesc(Long memberId);

    // 품종별 반려견 조회
    List<Pet> findByBreedOrderByCreatedDateDesc(String breed);

    // 반려견 이름으로 조회
    List<Pet> findByPetNameContaining(String petName);

    // 회원별 반려견 수 조회
    @Query("SELECT COUNT(p) FROM Pet p WHERE p.member.id = :memberId")
    Long countByMemberId(Long memberId);
}