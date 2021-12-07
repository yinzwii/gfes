package com.gfes.service;

import com.gfes.entity.User;
import com.gfes.repository.UserRepository;
import com.gfes.util.Md5Util;
import com.gfes.util.UuidUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@Service
public class UserService {

	private final UserRepository repository;

	public UserService(UserRepository repository) {
		this.repository = repository;
	}


	public User queryUser(String username,String password){
		return repository.findByNameAndPassword(username,password);
	}

	public User queryById(String id) {
		return repository.findById(id).orElse(null);
	}

	public User saveOrUpdateUser(User user,boolean changePwd) {
		if (changePwd){
			user.setPassword(Md5Util.MD5(user.getPassword()));
		}
		return repository.save(user);
	}

	public User add(User user) throws Exception {
		if (repository.countByName(user.getName()) >= 1){
			throw new Exception("用户名已存在！");
		}
		user.setPassword(Md5Util.MD5(user.getPassword()));
		user.setCreateTime(new Date());
		user.setId(UuidUtil.idNoline());
		return  repository.save(user);
	}


	public void delete(String id ){
		repository.deleteById(id);
	}


	public User update(User user) throws Exception {
		try {
			user = repository.save(user);
		} catch (Exception e) {
			throw new Exception("用户名已存在！");
		}
		return  user;
	}


	public User query(String id){
		return repository.findById(id).get();
	}


	public Page<User> queryPage(Pageable pageable){
		return repository.findAll(pageable);
	}

	public Page<User> queryPage(Pageable pageable,int type){
		if (type == -1){
			return repository.findAll(pageable);
		}
		Specification<User> specification = (root,query,cb) -> {
			Predicate predicate = cb.conjunction();
			List<Expression<Boolean>> expressions = predicate.getExpressions();
			expressions.add(cb.equal(root.get("type"),type));
			return  predicate;
		};
		return repository.findAll(specification,pageable);
	}


    public Vector<Vector> getUserList() {
		Vector<Vector> rows = new Vector<Vector>();
		List<Object[]>  list =  this.repository.getUserList();
		if (!list.isEmpty()) {
			for (Object[] object : list) {
				Vector temp = new Vector<String>();
				for (int i = 0; i < object.length; i++) {
					temp.add(object[i]);
				}
				rows.add(temp);
			}
		}
		return rows;
    }

    public Vector<Vector> selectListByFileId(String id) {
		Vector<Vector> rows = new Vector<Vector>();
		List<Object[]>  list =  this.repository.selectListByFileId(id);
		if (!list.isEmpty()) {
			for (Object[] object : list) {
				Vector temp = new Vector<String>();
				for (int i = 0; i < object.length; i++) {
					if (object[2].toString().equals("1")) {
						object[2] = "可读";
					} else if (object[2].toString().equals("2")){
						object[2] = "可写";
					}
					temp.add(object[i]);
				}
				rows.add(temp);
			}
		}
		return rows;
    }
}
