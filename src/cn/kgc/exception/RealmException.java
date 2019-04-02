package cn.kgc.exception;

public class RealmException extends Exception {
	
	public RealmException() {
	}
	
	public RealmException(String msg) {
		super(msg);
	}
	
	public RealmException(Throwable throwable) {
		super(throwable);
	}

}
