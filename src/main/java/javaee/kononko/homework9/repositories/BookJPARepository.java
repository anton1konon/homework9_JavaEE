package javaee.kononko.homework9.repositories;

import javaee.kononko.homework9.models.Book;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookJPARepository extends PagingAndSortingRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
}
