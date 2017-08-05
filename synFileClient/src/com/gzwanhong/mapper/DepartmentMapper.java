package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.Department;                
                                                 
public interface DepartmentMapper {                    
                                                 
	public Department queryById(String id);               
                                                 
	public List<Department> queryByIds(List<String> ids); 
                                                 
	public int save(Department department);                     
                                                 
	public int saveAll(List<Department> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(Department department);                   
                                                 
	public int updateAll(List<Department> list);          
}                                                
