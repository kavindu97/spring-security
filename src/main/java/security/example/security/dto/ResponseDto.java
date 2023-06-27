package security.example.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    private Integer code;
    private  String mzg;
    private Object data;
}
