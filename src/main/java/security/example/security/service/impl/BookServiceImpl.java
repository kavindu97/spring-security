package security.example.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.example.security.dto.BookDto;
import security.example.security.dto.ResponseDto;
import security.example.security.model.Book;
import security.example.security.repository.BookRepository;
import security.example.security.service.BookService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Override
    public ResponseDto insertData(BookDto bookDto) {
        ResponseDto responseDto=new ResponseDto();
        Book book =new Book();
        book.setBook(bookDto.getBook());
        book.setBookId(bookDto.getBookId());
        bookRepository.save(book);

        responseDto.setCode(200);
        responseDto.setMzg("Data inserted");
        return responseDto;
    }

    @Override
    public ResponseDto allData() {
        ResponseDto responseDto=new ResponseDto();
       List<Book> bookList= bookRepository.findAll();
       responseDto.setData(bookList);
       return responseDto;
    }
}
