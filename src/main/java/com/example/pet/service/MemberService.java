// MemberService.java - Oracle 연동 및 실제 DB 연동
package com.example.pet.service;

import com.example.pet.domain.Member;
import com.example.pet.domain.Pet;
import com.example.pet.repository.MemberRepository;
import com.example.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, PetRepository petRepository) {
        this.memberRepository = memberRepository;
        this.petRepository = petRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member);
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    /**
     * 이메일과 반려견 정보로 회원가입
     */
    public Long joinWithPet(String name, String email, String password, String petName, String breed) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 회원 생성
        Member member = new Member(name, email, password);
        Member savedMember = memberRepository.save(member);

        // 반려견 생성
        Pet pet = new Pet(petName, breed, savedMember);
        petRepository.save(pet);

        return savedMember.getId();
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 로그인
     */
    public Optional<Member> login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    /**
     * 로그인 (반려견 정보 포함)
     */
    public Optional<Member> loginWithPets(String email, String password) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(email, password);
        if (member.isPresent()) {
            // 반려견 정보도 함께 조회
            return memberRepository.findMemberWithPets(member.get().getId());
        }
        return Optional.empty();
    }

    /**
     * 반려견 등록
     */
    public Long registerPet(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        return savedPet.getId();
    }

    /**
     * 회원에게 반려견 추가
     */
    public Long addPetToMember(Long memberId, String petName, String breed) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Pet pet = new Pet(petName, breed, member);
        Pet savedPet = petRepository.save(pet);
        return savedPet.getId();
    }

    /**
     * 전체 회원 조회
     */
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 ID로 조회
     */
    @Transactional(readOnly = true)
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    /**
     * 회원 ID로 조회 (반려견 포함)
     */
    @Transactional(readOnly = true)
    public Optional<Member> findOneWithPets(Long memberId) {
        return memberRepository.findMemberWithPets(memberId);
    }

    /**
     * 특정 회원의 반려견 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Pet> findPetsByMember(Member member) {
        return petRepository.findByMember(member);
    }

    /**
     * 회원 ID로 반려견 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Pet> findPetsByMemberId(Long memberId) {
        return petRepository.findByMemberIdOrderByCreatedDateDesc(memberId);
    }

    /**
     * 이메일 중복 체크
     */
    @Transactional(readOnly = true)
    public boolean isEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 회원 수 조회
     */
    @Transactional(readOnly = true)
    public long getMemberCount() {
        return memberRepository.count();
    }

    /**
     * 반려견 수 조회
     */
    @Transactional(readOnly = true)
    public long getPetCount() {
        return petRepository.count();
    }

    /**
     * 특정 회원의 반려견 수 조회
     */
    @Transactional(readOnly = true)
    public long getPetCountByMember(Long memberId) {
        return petRepository.countByMemberId(memberId);
    }
}