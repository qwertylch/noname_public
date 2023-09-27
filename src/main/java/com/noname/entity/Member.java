package com.noname.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import com.noname.enums.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 9,
        allocationSize = 1)
@Table(name = "member")
public class Member extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR")
    private Long memberId;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 15)
    @ColumnDefault("'ACTIVE'")
    private String status;
    

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = "ACTIVE";
        }
    }
    
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    @ToString.Exclude
    private MemberImage images;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Bidding> biddings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Chatting> chattings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    private List<ProductReply> replies = new ArrayList<>();


}
