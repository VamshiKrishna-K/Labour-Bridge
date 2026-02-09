package com.lb.Services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.lb.entities.Labourer;
import com.lb.entities.Users;


@Service
public class SqlLabourService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
   
   public void saveLabour(String name, Integer age,String phone,BigDecimal dailyWage, Long m_id) {
    String sql = "INSERT INTO labourers (name, age,phone, daily_wage, m_id) " +
                 "VALUES (?, ?,?, ?, ?)";

    jdbcTemplate.update(sql, name, age,phone, dailyWage, m_id);
  }
   // Method to count labourers by mediator ID
    public int countLabourersByMediator(Long mediatorId) {
        String sql = "SELECT COUNT(*) FROM labourers WHERE m_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mediatorId);
        return count != null ? count : 0;
    }

      // Method to get all labourers for a given mediator
    public List<Labourer> getLabourersByMediator(Long mediatorId) {
        String sql = "SELECT * FROM labourers WHERE m_id = ?";

        return jdbcTemplate.query(sql, new Object[]{mediatorId}, (rs, rowNum) -> {
            Labourer labourer = new Labourer();
            labourer.setId(rs.getLong("id"));
            labourer.setName(rs.getString("name"));
            labourer.setAge(rs.getInt("age"));
            labourer.setDailyWage(rs.getBigDecimal("daily_wage"));
            labourer.setPhone(rs.getString("phone"));
            // If you want, you can also map the mediator field
            // labourer.setMediator(new Users(rs.getLong("m_id"))); 
            return labourer;
        });
    }
     public Integer count(){
        String sql="SELECT COUNT(*) FROM labourers";
        int c=jdbcTemplate.queryForObject(sql,Integer.class);
        
        return c;

       }
    public List<Labourer> getAllLabourers() {

    String sql = """
        SELECT l.id, l.name, l.age, l.daily_wage, l.phone,
               u.id AS mediator_id, u.name AS mediator_name
        FROM labourers l
        JOIN users u ON l.m_id = u.id
    """;

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
        Labourer labourer = new Labourer();
        labourer.setId(rs.getLong("id"));
        labourer.setName(rs.getString("name"));
        labourer.setAge(rs.getInt("age"));
        labourer.setDailyWage(rs.getBigDecimal("daily_wage"));
        labourer.setPhone(rs.getString("phone"));

        Users mediator = new Users();
        mediator.setId(rs.getLong("mediator_id"));
        mediator.setName(rs.getString("mediator_name"));
        labourer.setMediator(mediator);

        return labourer;
    });
}

}

