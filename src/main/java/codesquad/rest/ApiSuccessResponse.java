package codesquad.rest;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApiSuccessResponse<T> {
    private T data;
    private LocalDate time;

    public ApiSuccessResponse(T data){
        this.data = data;
        this.time = LocalDate.now();
    }

}
