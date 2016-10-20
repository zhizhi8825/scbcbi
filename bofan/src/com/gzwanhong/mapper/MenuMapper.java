package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.Menu;                
                                                 
public interface MenuMapper {                    
                                                 
	public Menu queryById(String id);               
                                                 
	public List<Menu> queryByIds(List<String> ids); 
                                                 
	public int save(Menu menu);                     
                                                 
	public int saveAll(List<Menu> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(Menu menu);                   
                                                 
	public int updateAll(List<Menu> list);          
}                                                
