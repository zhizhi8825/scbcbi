package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.RelationRoleMenu;                
                                                 
public interface RelationRoleMenuMapper {                    
                                                 
	public RelationRoleMenu queryById(String id);               
                                                 
	public List<RelationRoleMenu> queryByIds(List<String> ids); 
                                                 
	public int save(RelationRoleMenu relationrolemenu);                     
                                                 
	public int saveAll(List<RelationRoleMenu> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(RelationRoleMenu relationrolemenu);                   
                                                 
	public int updateAll(List<RelationRoleMenu> list);          
}                                                
