package com.neu.SP01.po;
//全局响应数据结构类
//表示出数据状态
public class ResponseBean<T> {
	//数据处理后的状态码（200成功，5XX失败）
	private Integer status;
	
	//数据处理后的消息（字符串表示），给普通用户看
	private String msg;
	
	private T data;

	@Override
	public String toString() {
		return "ResponseBean{" +
				"status=" + status +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	// 完整参数的构造函数
    public ResponseBean(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应的构造函数
    public ResponseBean(T data) {
        this(200, "success", data);
    }

    // 错误响应的构造函数
    public ResponseBean(Integer status, String msg) {
        this(status, msg, null);
    }


	public ResponseBean() {
		super();
	}
}
