package com.gzwanhong.dao.impl;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.UserDao;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.mapper.UserMapper;
import com.gzwanhong.utils.JsonUtil;

@Repository
@Scope(value = "prototype")
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

	public void test() throws Exception {
		BEGIN();
		SELECT("*");
		FROM("user");
		WHERE("create_time < #{time}");

		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("sql", SQL());
		map.put("sqlStr", "delete from user where user_name = #{userName}");
		map.put("time", new Date());
		map.put("userName", "hshs");
		// List<User> list = sqlSession.selectList(
		// "com.gzwanhong.mapper.UserMapper.queryBySql", map);
		// System.out.println(JsonUtil.beanToJson(list));
		// sqlSession.delete(
		// "com.gzwanhong.mapper.UserMapper.removeByExample", map);

		List<User> list = new ArrayList<User>();
		User user = new User();
		user.setUserName("hshs");
		user.setPassword("aaaa");
		list.add(user);

		user = new User();
		user.setUserName("hshs3");
		user.setPassword("aaaa");
		user.setRemark("我的人");
		list.add(user);

		Logger log = Logger.getLogger(this.getClass());
		// this.save(user);
		// this.saveAll(list);

		map = new HashMap<String, Object>();
		// map.put("userName", "luo4");
		// map.put("showName", "luo");
		user = new User();
		// user.setId("4DC6AF5B20FE4506AB616B1FD40DF060");
		// user.setUserName("luo3");
		// user.setPassword("222");
		// user.setShowName("luo2");
		// user.setDepartmentId("DA8DD26F9CCB4FFB9BFDD89F3EE4B77C");
		// user.setRoleId("0791D4A6D03A476CBE4387B1B0B0399F" );
		user.setRemark("2");
		System.out.println(JsonUtil.beanToJson(queryBySqlToDatagrid(
				UserMapper.class, "getUsers", map)));

	}
}
