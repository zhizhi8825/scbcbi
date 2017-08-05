package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.DownAllFile;                
                                                 
public interface DownAllFileMapper {                    
                                                 
	public DownAllFile queryById(String id);               
                                                 
	public List<DownAllFile> queryByIds(List<String> ids); 
                                                 
	public int save(DownAllFile downallfile);                     
                                                 
	public int saveAll(List<DownAllFile> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(DownAllFile downallfile);                   
                                                 
	public int updateAll(List<DownAllFile> list);          
}                                                
