package com.banking_app;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.banking_app.dao.UserDao;
import com.banking_app.model.User;
import com.banking_app.service.impl.UserServiceImpl;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class UserServiceTest {
	
    @InjectMocks
    UserServiceImpl userService;
     
    @Mock
    UserDao dao;
 
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
     
    @Test
    public void getAllUsers()
    {
        List<User> list = new ArrayList<User>();
        User empOne = new User( "John", "John", "howtodoin java","20/12/2009","Savings","Ikeja Lagos",1000,"",null,true,true);
        User empTwo = new User( "John1", "John", "howtodoin java","20/12/2009","Savings","Ikeja Lagos",1000,"",null,true,true);
        User empThree = new User( "John2", "John", "howtodoin java","20/12/2009","Savings","Ikeja Lagos",1000,"",null,true,true);
         
        list.add(empOne);
        list.add(empTwo);
        list.add(empThree);
         
        when(dao.findAll()).thenReturn(list);
         
        //test
        List<User> empList = userService.findAll();
        //check if the size of the list is 3
        assertEquals(3, empList.size());
        //verify if it calls findAll 1 time in dao
        verify(dao, times(1)).findAll();
    }
    
    @Test
    public void saveUser()
    {
        //List<User> list = new ArrayList<User>();
        User empOne = new User( "John", "John", "howtodoin java","20/12/2009","Savings","Ikeja Lagos",1000,"",null,true,true);
 
         
        when(dao.save(empOne)).thenReturn(empOne);
         
        //test
        User empList = userService.save(empOne);
         
       //assertEquals(3, empList.size());
        verify(dao, times(1)).save(empOne);
    }
    
    @Test
    public void testloadUserByUsername()
    {
        //List<User> list = new ArrayList<User>();
        User empOne = new User( "John", "John", "howtodoin java","20/12/2009","Savings","Ikeja Lagos",1000,"",null,true,true);
 
         
        when(dao.save(empOne)).thenReturn(empOne);
         
        //test
        User empList = userService.save(empOne);
         
       //assertEquals(3, empList.size());
        verify(dao, times(1)).save(empOne);
    }
    
    
     
}
