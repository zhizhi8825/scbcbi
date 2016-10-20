package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.ChangeRecord;                
                                                 
public interface ChangeRecordMapper {                    
                                                 
	public ChangeRecord queryById(String id);               
                                                 
	public List<ChangeRecord> queryByIds(List<String> ids); 
                                                 
	public int save(ChangeRecord changerecord);                     
                                                 
	public int saveAll(List<ChangeRecord> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(ChangeRecord changerecord);                   
                                                 
	public int updateAll(List<ChangeRecord> list);          
}                                                
