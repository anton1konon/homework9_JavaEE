package javaee.kononko.homework9.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    private String password;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_to_permissions", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<PermissionEntity> permissions;
}
