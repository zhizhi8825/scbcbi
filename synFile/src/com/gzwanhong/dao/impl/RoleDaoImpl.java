package com.gzwanhong.dao.impl;                                    
                                                                   
import org.springframework.context.annotation.Scope;               
import org.springframework.stereotype.Repository;                  
                                                                   
import com.gzwanhong.dao.RoleDao;                                  
                                                                   
@Repository                                                        
@Scope(value = "prototype")                                        
public class RoleDaoImpl extends BaseDaoImpl implements RoleDao {  
                                                                   
}                                                                  
