package mng.qlkt.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@Getter @Setter
public class SignUpForm {
    private String name;
    private String username;
    private String address;
    private Date birth;
    private Date workingday;
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String idUser;
    private Integer isActive;
    private Set<String> roles;

    public SignUpForm() {
    }

    public SignUpForm(String name, String username, String email, String password,Integer isActive, Set<String> roles, String gender, String phone) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.gender = gender;
        this.phone = phone;
        this.isActive = isActive;
    }


}
