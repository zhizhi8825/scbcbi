package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.SynRecord;                
                                                 
public interface SynRecordMapper {                    
                                                 
	public SynRecord queryById(String id);               
                                                 
	public List<SynRecord> queryByIds(List<String> ids); 
                                                 
	public int save(SynRecord synrecord);                     
                                                 
	public int saveAll(List<SynRecord> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(SynRecord synrecord);                   
                                                 
	public int updateAll(List<SynRecord> list);          
}                                                
