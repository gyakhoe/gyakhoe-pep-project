package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public Account selectAccountByUsername(String username)throws SQLException {
        String query = "select * from account where username = ?";
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet result  = preparedStatement.executeQuery();
        while(result.next()) {
           return new Account( result.getInt("account_id"), result.getString("username"), result.getString("password"));
        }
        return null;
    }


    public Account insertAccount(Account account) throws SQLException {

        Connection databaseConnection = ConnectionUtil.getConnection();

        String insertQuery = "insert into account(username, password) values(?,?)";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        int count = preparedStatement.executeUpdate();
        System.out.println(count);
        ResultSet result =  preparedStatement.getGeneratedKeys();
        
        if(result.next()) {
            int primaryKey = (int) result.getLong(1);
            return new Account(primaryKey, account.getUsername(), account.getPassword());
        }



        return null;
    }

    
}
