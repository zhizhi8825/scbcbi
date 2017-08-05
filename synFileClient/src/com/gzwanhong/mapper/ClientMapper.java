package com.gzwanhong.mapper;                    
                                                 
import java.util.List;                           
                                                 
import com.gzwanhong.domain.Client;                
                                                 
public interface ClientMapper {                    
                                                 
	public Client queryById(String id);               
                                                 
	public List<Client> queryByIds(List<String> ids); 
                                                 
	public int save(Client client);                     
                                                 
	public int saveAll(List<Client> list);            
                                                 
	public int removeById(String id);               
                                                 
	public int removeByIds(List<String> ids);       
                                                 
	public int update(Client client);                   
                                                 
	public int updateAll(List<Client> list);          
}                                                
