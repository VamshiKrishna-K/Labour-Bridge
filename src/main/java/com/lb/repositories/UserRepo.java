/* package com.lb.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lb.entities.User;
//To interact with data base
@Repository("UserRepo")
public interface UserRepo extends JpaRepository<User,Long>{
  //extra methods db relationoperations
  //custom query methods
  //custom finder methods

  Optional<User> findByEmail(String Email);
  Optional<User> findByEmailAndPassword(String email,String password);
}
 */