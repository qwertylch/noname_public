package com.noname.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "ADMIN_SEQ_GENERATOR",
        sequenceName = "ADMIN_SEQ",
        initialValue = 1,
        allocationSize = 1)
@Table(name = "admin")
public class Admin extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ADMIN_SEQ_GENERATOR")
    private Long adminId;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String password;





}
