package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.ChangeFile;                
                                                 
public interface ChangeFileMapper {                    
                                                 
	public ChangeFile queryById(String id);               
                                                 
	public List<ChangeFile> queryByIds(List<String> ids); 
                                                 
	public int save(ChangeFile changefile);                     
                                                 
	public int saveAll(List<ChangeFile> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(ChangeFile changefile);                   
                                                 
	public int updateAll(List<ChangeFile> list);          
}                                                
