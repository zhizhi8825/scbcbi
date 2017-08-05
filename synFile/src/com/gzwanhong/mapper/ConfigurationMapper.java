package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.Configuration;                
                                                 
public interface ConfigurationMapper {                    
                                                 
	public Configuration queryById(String id);               
                                                 
	public List<Configuration> queryByIds(List<String> ids); 
                                                 
	public int save(Configuration configuration);                     
                                                 
	public int saveAll(List<Configuration> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(Configuration configuration);                   
                                                 
	public int updateAll(List<Configuration> list);          
}                                                
