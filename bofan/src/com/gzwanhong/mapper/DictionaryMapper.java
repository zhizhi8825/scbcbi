package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.Dictionary;                
                                                 
public interface DictionaryMapper {                    
                                                 
	public Dictionary queryById(String id);               
                                                 
	public List<Dictionary> queryByIds(List<String> ids); 
                                                 
	public int save(Dictionary dictionary);                     
                                                 
	public int saveAll(List<Dictionary> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(Dictionary dictionary);                   
                                                 
	public int updateAll(List<Dictionary> list);          
}                                                
