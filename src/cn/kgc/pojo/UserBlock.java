package cn.kgc.pojo;

public class UserBlock {
	
	private String userId;
	private int blockId;
	
	public UserBlock() {
	}

	public UserBlock(String userId, int blockId) {
		this.userId = userId;
		this.blockId = blockId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	
}
