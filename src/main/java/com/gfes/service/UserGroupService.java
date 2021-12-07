package com.gfes.service;

import com.gfes.entity.UserGroup;
import com.gfes.repository.UserGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

@Service
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public List<UserGroup> selectGroupIdByUserId(String id) {
        return this.userGroupRepository.selectGroupIdByUserId(id);
    }

    public List<UserGroup>  selectGroupUserByGroupId(String groupId) {
        return this.userGroupRepository.selectGroupUserByGroupId(groupId);
    }

    public Vector<Vector> converVector(List<Object[]>  allList) {
        Vector<Vector> rows = new Vector<Vector>();
        if (!allList.isEmpty()) {
            for (Object[] object : allList) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }

    public List<Object[]> selectListByUserIdAndGroupId(String userId, String groupId) {
        return this.userGroupRepository.selectListByUserIdAndGroupId(userId,groupId);
    }
}
