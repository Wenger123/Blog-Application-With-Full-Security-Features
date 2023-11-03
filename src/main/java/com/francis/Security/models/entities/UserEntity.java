package com.francis.Security.models.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_Table")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    @Column(nullable = false,unique = true)
    private String username;
    @Email
    @Column(nullable = false,unique = true)
    private String email;
    private String password;
    @Transient
    private String confirmPassword;
    private String phoneNumber;
    private String address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_Id"),
               inverseJoinColumns = @JoinColumn(name = "role_Id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments;

}
