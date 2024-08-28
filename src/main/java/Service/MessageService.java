package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message) throws SQLException {

        if (message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }

        final Message addedMessage = messageDAO.insertMessage(message);
        return addedMessage;
    }

    public List<Message> getAllMessages() {
        return messageDAO.selectAllMessage();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.selectMessageById(messageId);
    }

    public Message removeMessageById(int messageId) throws SQLException {
        final Message messageToDelete = getMessageById(messageId);
        if (messageToDelete.getMessage_text().equals("")) {
            System.out.println("No message was found in table for message ID: " + messageId);
            return null;
        }
        int deletedCount = messageDAO.deleteMessageByID(messageId);
        if (deletedCount == 1) {
            System.out.println("Message was successfully deleted");
            return messageToDelete;
        } else {
            System.out.println("No message was deleted");
            return null;
        }
    }

    public List<Message> getAllMessagesByUserId(int userId) throws SQLException {
        return messageDAO.selectAllMessageByUserId(userId);
    }

    public Message updateMessageTextByMessageId(int messageId, String messageText) throws SQLException {
        if (messageText.isEmpty() || messageText.length() > 255) {
            return null;
        }
        final Message messageToUpdate = getMessageById(messageId);
        if (messageToUpdate.getMessage_id() == 0 && messageToUpdate.getMessage_text().equals("")) {
            return null;
        }
        int updateCount = messageDAO.updateMessageTextById(messageId, messageText);
        if (updateCount != 1) {
            return null;
        }
        final Message messageToSend = messageToUpdate;
        messageToSend.setMessage_text(messageText);
        return messageToSend;
    }

}
