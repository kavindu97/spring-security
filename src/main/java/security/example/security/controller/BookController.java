package security.example.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.example.security.dto.BookDto;
import security.example.security.dto.ResponseDto;
import security.example.security.service.BookService;
@RestController
@RequestMapping("dekmo/admlin/book")
@CrossOrigin
public class BookController {
    @Autowired
    private BookService bookService;
    @PostMapping("/save")
    public ResponseDto insertBook(@RequestBody BookDto bookDto){

        return bookService.insertData(bookDto);
    }

}
