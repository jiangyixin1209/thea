package top.jiangyixin.thea.core.exception;

/**
 * Thea 异常类
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午2:34
 */
public class TheaException extends RuntimeException {
	
	public TheaException(String message) {
		super(message);
	}
	
	public TheaException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public TheaException(Throwable throwable) {
		super(throwable);
	}
	
}
