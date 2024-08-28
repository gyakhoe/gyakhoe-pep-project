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

        if(message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255){
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


    
}
