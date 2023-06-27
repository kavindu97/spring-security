package security.example.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.example.security.model.Book;
@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
}
