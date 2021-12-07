package com.gfes.service;

import com.gfes.entity.User;
import com.gfes.entity.UserGroup;
import com.gfes.repository.GroupRepository;
import com.gfes.repository.UserGroupRepository;
import com.gfes.repository.UserRepository;
import com.gfes.util.UuidUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.gfes.entity.Group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService  {

	private final GroupRepository dao;

	private final UserRepository userRepository;

	private final UserGroupRepository userGroupRepository;

	public GroupService(GroupRepository dao, UserRepository userRepository,
						UserGroupRepository userGroupRepository) {
		this.dao = dao;
		this.userRepository = userRepository;
		this.userGroupRepository = userGroupRepository;
	}

	public Group add(Group group) throws Exception {
		if (dao.countByName(group.getName()) >= 1){
			throw new Exception("当前群组已存在！");
		}
		group.setCreateTime(new Date());
		group.setId(UuidUtil.idNoline());
		return  dao.save(group);
	}


	public void delete(String id ){
		dao.deleteById(id);
	}


	public Group update(String id,String name) throws Exception {
		Group group = dao.findById(id).get();
		group.setName(name);
		try {
			group = dao.save(group);
		} catch (Exception e) {
			throw new Exception("群组名称已存在！");
		}
		return  group;
	}


	public Group query(String id){
		return dao.findById(id).get();
	}


	public Page<Group> queryPage(Pageable pageable){
		return dao.findAll(pageable);
	}

	public List<Group> queryList() {
		return dao.findAll();
	}

	public List<Group> queryMyGroupList(String userId){
		if ("78d5bc915240430b899527acc55bcf66".equals(userId)){
			return dao.findAll();
		}
		return dao.findGroupByUserId(userId);
	}

    public String queryGroupByName(String groupName) {
		return dao.queryGroupByName(groupName);
    }

	public List<String> queryMembers(String groupId){
		return dao.queryMembers(groupId);
	}

	public List<String> queryNonmembers(String groupId){
		return dao.queryNonmembers(groupId);
	}

	public void addMembers(List<String> members,String groupId){
		Date now = new Date();
		members.parallelStream()
				.forEach(x ->{
					User user = userRepository.findByName(x);
					if (user != null){
						UserGroup userGroup = UserGroup.builder()
								.id(UuidUtil.idNoline())
								.createTime(now)
								.groupId(groupId)
								.userId(user.getId())
								.build();
						userGroupRepository.save(userGroup);
					}
				});
	}

	public void deleteMembers(List<String> members,String groupId){
		members.parallelStream()
				.forEach(x ->{
					User user = userRepository.findByName(x);
					if (user != null){
						userGroupRepository.deleteByUserIdAndGroupId(user.getId(),groupId);
					}
				});
	}
}
