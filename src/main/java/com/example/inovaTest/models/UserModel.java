package com.example.inovaTest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.inovaTest.enums.GenderRole;
import com.example.inovaTest.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Login is required")
    private String login;
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    private boolean verifiedEmail;

    private boolean enabled;



    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private GenderRole gender;

    @Column(nullable = false)
    private LocalDate  dateOfBirth;

    private String profilePicture; // path da foto de perfil

    private String profileDescription; // bio 

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<FriendshipModel> sentFriendRequests;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<FriendshipModel> receivedFriendRequests;



    public UserModel(String login, String password, String email, String gender, LocalDate  dateOfBirth ){
        this.login = login;
        this.password = password;
        this.role = UserRole.USER;
        this.email = email;
        this.verifiedEmail = false;
        this.enabled = true;
        this.gender = GenderRole.fromString(gender);
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }



   public List<UserModel> getFriends(List<FriendshipModel> friendships) {
    return friendships.stream()
        .filter(FriendshipModel::isAccepted)
        .map(f -> f.getSender().equals(this) ? f.getReceiver() : f.getSender())
        .toList();
}

}
