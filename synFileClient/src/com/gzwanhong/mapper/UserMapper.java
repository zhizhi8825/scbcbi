package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.User;                
                                                 
public interface UserMapper {                    
                                                 
	public User queryById(String id);               
                                                 
	public List<User> queryByIds(List<String> ids); 
                                                 
	public int save(User user);                     
                                                 
	public int saveAll(List<User> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(User user);                   
                                                 
	public int updateAll(List<User> list);          
}                                                
