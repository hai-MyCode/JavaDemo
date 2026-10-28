package csc.zjzw.csc.common;

public class BusinessException extends RuntimeException {
    private final Integer code;
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
    /** 自定义业务码，比如 1001 = 余额不足 */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public Integer getCode() {
        return code;
    }
}
