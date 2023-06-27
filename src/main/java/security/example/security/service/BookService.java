package security.example.security.service;

import security.example.security.dto.BookDto;
import security.example.security.dto.ResponseDto;

public interface BookService {

    ResponseDto insertData(BookDto bookDto);
    ResponseDto allData();
}
