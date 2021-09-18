package cn.cyejing.dam.core.exception;


import lombok.Getter;

public class DamException extends RuntimeException{

    private static final long serialVersionUID = -5658789202509039759L;

    @Getter
    private ErrorCode errorCode;

    public DamException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
