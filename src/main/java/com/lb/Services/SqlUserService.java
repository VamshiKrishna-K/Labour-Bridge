package com.lb.Services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import com.lb.entities.Role;
import com.lb.entities.Users;
import com.lb.entities.WorkRequest;
import com.lb.entities.WorkStatus;




@Service
public class SqlUserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean verifyLogin(String email, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, password);
        return count != null && count > 0;
    }
   

    public void saveUser(String name, String email, String password, String phone, String role) {
    
    String insertUserSql = "INSERT INTO Users (name, email, password, phone, role, enabled, email_verified) " +
                           "VALUES (?, ?, ?, ?, ?, true, false)";
    jdbcTemplate.update(insertUserSql, name, email, password, phone, role);

   
    Long userId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

    switch (role) {
        case "Mediator":
            jdbcTemplate.update("INSERT INTO mediators (user_id) VALUES (?)", userId);
            break;

        case "Constructor":
            jdbcTemplate.update("INSERT INTO constructors (user_id) VALUES (?)", userId);
            break;

        case "Admin":
            
            break;

       
    }
}


    public void saveWorkRequest(Long constructorId, String region, Integer numLabourers, java.time.LocalDate startDate,
            WorkStatus status) {
        String sql = "INSERT INTO work_requests (constructor_id, region, num_labourers, start_date, status,mediator_id) VALUES (?, ?, ?, ?, ?,?)";
        jdbcTemplate.update(sql, constructorId, region, numLabourers, Date.valueOf(startDate), status,null);
    }

    public Users getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { email }, (rs, rowNum) -> {
            Users user = new Users();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPhone(rs.getString("phone"));
            user.setRole(Role.valueOf(rs.getString("role")));
            return user;
        });

    }

    public Integer count(String roles) {
        String sql = "SELECT COUNT(*) FROM Users where role=?";
        int c = jdbcTemplate.queryForObject(sql, Integer.class, roles);
        return c;

    }
    public void saveWorkRequest(Long constructorId, String region, Integer numLabourers, LocalDate startDate,String status) {
        String sql = "INSERT INTO work_requests (constructor_id, mediator_id, region, num_labourers, start_date, status) " +
                     "VALUES (?, NULL, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, constructorId, region, numLabourers, startDate,status);
    }

    public String getRoleById(Long id){
        String sql="SELECT role FROM Users where id=?";
        String role=jdbcTemplate.queryForObject(sql,String.class,id);
        return role;
    }

    // Method to get all labourers for a given mediator
    public List<Users> getUsersByRole(String role) {
        String sql = "SELECT * FROM users WHERE role = ?";

        return jdbcTemplate.query(sql, new Object[] { role }, (rs, rowNum) -> {
            Users user = new Users();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPhone(rs.getString("phone"));
            user.setEnabled(rs.getBoolean("enabled"));
            
            return user;
        });
    }
  
public void handleApproval(Long id) {

   /*  String sql = "UPDATE Users SET enabled = NOT enabled WHERE id = ?";
    jdbcTemplate.update(sql, id); */
    String sql = "UPDATE Users SET enabled = CASE WHEN enabled = 1 THEN 0 ELSE 1 END WHERE id = ?";
    int rows = jdbcTemplate.update(sql, id);
    System.out.println("Rows updated: " + rows);

}


public Users getUserById(Long id) {
      String sql = "SELECT * FROM users WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {
            Users user = new Users();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPhone(rs.getString("phone"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setRole(Role.valueOf(rs.getString("role")));
            return user;
        });



}

public List<WorkRequest> getWorkRequestsByConstructorId(Long id) {
    String sql = """
        SELECT wr.id, wr.region, wr.num_labourers, wr.start_date, wr.status,
               wr.mediator_id, u.name AS mediator_name
        FROM work_requests wr
        LEFT JOIN users u ON wr.mediator_id = u.id
        WHERE wr.constructor_id = ?
    """;

    return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
        WorkRequest workRequest = new WorkRequest();

        workRequest.setId(rs.getLong("id"));
        workRequest.setRegion(rs.getString("region"));
        workRequest.setNumLabourers(rs.getInt("num_labourers"));
        workRequest.setStartDate(rs.getDate("start_date").toLocalDate());

        // Convert DB string to enum safely
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                workRequest.setStatus(WorkStatus.valueOf(statusStr));
            } catch (IllegalArgumentException e) {
                workRequest.setStatus(null); // handle invalid status gracefully
            }
        }

        // Handle mediator name
        @SuppressWarnings("unused")
        Long mediatorId = rs.getLong("mediator_id");
        if (rs.wasNull()) {
            workRequest.setMediatorName("Not Assigned");
        } else {
            workRequest.setMediatorName(rs.getString("mediator_name"));
        }

        return workRequest;
    });
}


public Integer getCountOfPendingWorks(Long id) {
    
    String sql = "SELECT COUNT(*) FROM work_requests WHERE constructor_id = ? AND status = ?";
   return jdbcTemplate.queryForObject(sql,Integer.class,id,"Pending");
}


public List<WorkRequest> getAllPendingRequests() {
    String sql = """
        SELECT wr.id, wr.region, wr.num_labourers, wr.start_date, wr.status,
               wr.constructor_id,
               u.name AS constructor_name
        FROM work_requests wr
        JOIN users u ON wr.constructor_id = u.id
        WHERE wr.status = 'PENDING'
    """;

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
        WorkRequest wr = new WorkRequest();
        wr.setId(rs.getLong("id"));
        wr.setRegion(rs.getString("region"));
        wr.setNumLabourers(rs.getInt("num_labourers"));
        wr.setStartDate(rs.getDate("start_date").toLocalDate());
        wr.setStatus(WorkStatus.valueOf(rs.getString("status")));
        wr.setConstructorName(rs.getString("constructor_name"));
      
        return wr;
    });
}

    public void acceptWorkRequest(Long requestId, Long mediatorId) {
        String sql = "UPDATE work_requests SET mediator_id = ?, status = 'APPROVED' WHERE id = ?";
        int rows = jdbcTemplate.update(sql, mediatorId, requestId);

        if (rows == 0) {
            System.out.println(" No work request updated for ID: " + requestId);
        } else {
            System.out.println(" Work request " + requestId + " accepted by mediator " + mediatorId);
        }
    }

}