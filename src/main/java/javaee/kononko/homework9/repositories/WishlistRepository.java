package javaee.kononko.homework9.repositories;

import javaee.kononko.homework9.models.Book;
import javaee.kononko.homework9.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    @Query("select case when count(w)>0 then true else false end from Wishlist w join w.books b where w.userId = :user and b.Id = :book")
    boolean existsByLoginAndBook_Id(Integer user, Integer book);

    @Query("select w.books from Wishlist w where w.userId = :user")
    List<Book> getWishlistBooks(Integer user);
}
