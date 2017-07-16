package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.TrackRecord;                
                                                 
public interface TrackRecordMapper {                    
                                                 
	public TrackRecord queryById(String id);               
                                                 
	public List<TrackRecord> queryByIds(List<String> ids); 
                                                 
	public int save(TrackRecord trackrecord);                     
                                                 
	public int saveAll(List<TrackRecord> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(TrackRecord trackrecord);                   
                                                 
	public int updateAll(List<TrackRecord> list);          
}                                                
