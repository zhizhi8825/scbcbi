package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.Role;                
                                                 
public interface RoleMapper {                    
                                                 
	public Role queryById(String id);               
                                                 
	public List<Role> queryByIds(List<String> ids); 
                                                 
	public int save(Role role);                     
                                                 
	public int saveAll(List<Role> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(Role role);                   
                                                 
	public int updateAll(List<Role> list);          
}                                                
