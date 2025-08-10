// Pet.java - Oracle 테이블에 맞춘 버전
package com.example.pet.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PET")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_seq")
    @SequenceGenerator(name = "pet_seq", sequenceName = "SEQ_PET_ID", allocationSize = 1)
    @Column(name = "PET_ID")
    private Long id;

    @Column(name = "PET_NAME", nullable = false, length = 50)
    private String petName;

    @Column(name = "BREED", nullable = false, length = 100)
    private String breed;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    // 외래키: 어떤 회원의 반려견인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    // 기본 생성자
    public Pet() {}

    // 생성자
    public Pet(String petName, String breed, Member member) {
        this.petName = petName;
        this.breed = breed;
        this.member = member;
        this.createdDate = LocalDateTime.now();
    }

    // Getter, Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
}
