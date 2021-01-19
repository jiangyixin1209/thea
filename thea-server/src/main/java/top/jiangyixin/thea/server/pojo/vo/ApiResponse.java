package top.jiangyixin.thea.server.pojo.vo;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/23 上午11:47
 */
public class ApiResponse<T> {
	
	public final static int SUCCESS_CODE = 200;
	public final static int ERROR_CODE = 500;
	
	private int code;
	private String message;
	private T data;
	
	public ApiResponse() {
	}
	
	public ApiResponse(int code, T data) {
		this.code = code;
		this.data = data;
	}
	
	public ApiResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public ApiResponse(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public static ApiResponse<String> success() {
		return new ApiResponse<>(SUCCESS_CODE, "操作成功");
	}
	
	public static ApiResponse<String> success(String message) {
		return new ApiResponse<>(SUCCESS_CODE, message);
	}
	
	public static ApiResponse<String> fail() {
		return new ApiResponse<>(ERROR_CODE, null);
	}
	
	public static ApiResponse<String> fail(String message) {
		return new ApiResponse<>(ERROR_CODE, message);
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
}
