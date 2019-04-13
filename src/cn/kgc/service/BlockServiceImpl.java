package cn.kgc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.BlockMapper;
import cn.kgc.pojo.Block;
import cn.kgc.pojo.User;

@Service
public class BlockServiceImpl implements BlockService {
	
	@Resource
	private BlockMapper blockMapper;

	@Override
	public List<Block> getBlocksByProjectId(int projectId) {
		return blockMapper.getByProjectId(projectId);
	}

	@Override
	public List<User> getUsersByBlockId(int blockId) {
		return blockMapper.getByBlockId(blockId);
	}

	@Override
	public int updateBlockLeader(String username, int blockId) {
		return blockMapper.updateLeaderId(username, blockId);
	}

	@Override
	public Block getBlockById(int blockId) {
		return blockMapper.getById(blockId);
	}

	@Override
	public int updateBlock(Block block) {
		return blockMapper.update(block);
	}

	@Override
	public int addBlock(Block block) {
		return blockMapper.add(block);
	}

	@Override
	public int getIncrement() {
		return blockMapper.getIncrement();
	}

}
