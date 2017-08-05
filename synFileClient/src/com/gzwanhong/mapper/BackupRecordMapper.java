package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.BackupRecord;                
                                                 
public interface BackupRecordMapper {                    
                                                 
	public BackupRecord queryById(String id);               
                                                 
	public List<BackupRecord> queryByIds(List<String> ids); 
                                                 
	public int save(BackupRecord backuprecord);                     
                                                 
	public int saveAll(List<BackupRecord> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(BackupRecord backuprecord);                   
                                                 
	public int updateAll(List<BackupRecord> list);          
}                                                
