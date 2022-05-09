package javaee.kononko.homework9.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Wishlist {
    @Id
    @Column(name = "userId")
    private Integer userId;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Book> books;
}
