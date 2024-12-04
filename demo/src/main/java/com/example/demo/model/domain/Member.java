package com.example.demo.model.domain;

import lombok.*; // 어노테이션 자동 생성
import jakarta.persistence.*; // 기존 javax 후속 버전

@Getter // setter는 없음(무분별한 변경 x)
@Entity // 아래 객체와 DB 테이블을 매핑. JPA가 관리
@Table(name = "member") // 테이블 이름을 지정. 없는 경우 클래스이름으로 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부 생성자 접근 방지

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 1씩 증가
    @Column(name = "id", updatable = false) // 수정 x
    private Long id;

    @Column(name = "name", nullable = false) // null x
    private String name = "";

    @Column(name = "email", unique = true, nullable = false) // unique 중복 x
    private String email = "";

    @Column(name = "password", nullable = false)
    private String password = "";

    @Column(name = "age", nullable = false)
    private String age = "";

    @Column(name = "mobile", nullable = false)
    private String mobile = "";

    @Column(name = "address", nullable = false)
    private String address = "";

    @Builder // 생성자에 빌더 패턴 적용(불변성)
    public Member(String name, String email, String password, String age, String mobile, String address){
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.mobile = mobile;
        this.address = address;
    }
    // @Id // 기본 키
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 1씩 증가
    // @Column(name = "id", updatable = false) // 수정 x
    // private Long id;
    // @Column(name = "title", nullable = false) // null x
    // private String title = "";
    // @Column(name = "content", nullable = false)
    // private String content = "";
    // @Column(name = "user", nullable = false)
    // private String user = "";
    // @Column(name = "newdate", nullable = false)
    // private String newdate = "";
    // @Column(name = "count", nullable = false)
    // private String count = "";
    // @Column(name = "likec", nullable = false)
    // private String likec = "";
    
    // @Builder // 생성자에 빌더 패턴 적용(불변성)
    // public Member(String title, String content){
    //     this.title = title;
    //     this.content = content;
    // }
    // public void update(String title, String content) { // 현재 객체 상태 업데이트
    //     this.title = title;
    //     this.content = content;
    // }
}