package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message insertMessage(Message message) throws SQLException {
        String query = "insert into message(posted_by, message_text, time_posted_epoch) values(?,?,?)";
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            Message insertedMessage = message;
            insertedMessage.setMessage_id(resultSet.getInt(1));
            return insertedMessage;
        }
        return null;
    }

    public List<Message> selectAllMessage() {
        final List<Message> messages = new ArrayList<>();
        String query = "select * from message";
        Connection connection = ConnectionUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            System.out.println("Exception occured while selecting all messages ");
            e.printStackTrace();
            return null;
        }
        return messages;
    }

    public Message selectMessageById(int messageId) {
        String query = "select * from message where message_id = ?";
        Connection connection = ConnectionUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch"));
            }
            System.out.println("Sending Empty Message");
            return new Message(0, 0, "", 0);
        } catch (SQLException e) {
            System.out.println("Exception occurred while fetching message on ID : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public int deleteMessageByID(int messageId) throws SQLException {
        String sql = "delete from message where message_id = ?";
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, messageId);
        return preparedStatement.executeUpdate();
    }

    public List<Message> selectAllMessageByUserId(int userID) throws SQLException {
        final List<Message> messages = new ArrayList<>();
        String query = "select * from message where posted_by = ?";
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userID);
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            messages.add(
                    new Message(
                            result.getInt("message_id"),
                            result.getInt("posted_by"),
                            result.getString("message_text"),
                            result.getLong("time_posted_epoch")));

        }
        return messages;
    }

    public int updateMessageTextById(int messageId, String messageText) throws SQLException {
        String query = "update message set message_text = ? where message_id = ?";
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, messageText);
        preparedStatement.setInt(2, messageId);
        return preparedStatement.executeUpdate();

    }

}
