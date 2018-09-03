package codesquad.domain;

import codesquad.dto.UserDTO;
import codesquad.exception.UserVerificationException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor @Getter
public class User {
    public static final String FIELD_NAME_EMAIL = "email";
    public static final String FIELD_NAME_PASSWORD = "password";
    public static final GuestUser GUEST_USER = new GuestUser();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 40, unique = true, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 14, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserPermissions permissions;



    public User(String email, String password, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.permissions = UserPermissions.NORMAL;
    }
    @Builder
    public User(long id, String email, String password, String name, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.permissions = UserPermissions.NORMAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email, password);
    }

    public void matchPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, password))
            throw new UserVerificationException(FIELD_NAME_PASSWORD, "비밀번호를 확인하기 바랍니다.");
    }

    public static User valueOf(UserDTO userDTO, PasswordEncoder passwordEncoder) {
        if (!userDTO.passwordConfirm())
            throw new UserVerificationException(FIELD_NAME_PASSWORD, "비밀번호를 확인하기 바랍니다.");
        return new User(userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getName(), userDTO.getPhoneNumber());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public boolean isGuestUser() {
        return false;
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }

    }


}
