package cn.kgc.exception;

public class ControllerException extends Exception {
	
	public ControllerException() {
	}
	
	public ControllerException(String msg) {
		super(msg);
	}
	
	public ControllerException(Throwable throwable) {
		super(throwable);
	}

}
