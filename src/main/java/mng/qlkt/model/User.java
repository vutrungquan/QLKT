package mng.qlkt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "userName"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @JsonIgnore
    @NotBlank
    @Size(min = 0, max = 100)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();
    private Date birth;
    @NotBlank
    @Size(min = 3, max = 200)
    private String address;
    private Date workingday;
    @CreatedDate
    private Date inTime = new Date();
    private String gender;
    private String phone;
    private String idUser;
    private Integer isActive;

    public User() {
    }

    public User(Long id, String name, String username, String email, String password, Set<Role> roles, Date birth, String address, Date workingday, Date inTime, String gender, String phone, String idUser) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.birth = birth;
        this.address = address;
        this.workingday = workingday;
        this.inTime = inTime;
        this.gender = gender;
        this.phone = phone;
        this.idUser = idUser;
    }

    public User(@NotBlank
                 @Size(min = 3, max = 50) String name,
                @NotBlank
                 @Size(min = 3, max = 50)String username,
                @NotBlank
                 @Size(max = 50)
                 @Email String email,
                @NotBlank
                 @Size(min = 0, max = 100)
                        String encode,
                String address,
                String gender,
                String phone,
                Date birth,
                String idUser,
                Date workingday
                ) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = encode;
        this.address = address;
        this.gender = gender;
        this.birth = birth;
        this.phone = phone;
        this.idUser = idUser;
        this.workingday = workingday;
    }
}
