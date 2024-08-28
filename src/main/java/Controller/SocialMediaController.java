package Controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registrationHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::messageCreationHandler);
        app.get("messages", this::messageRetrieveAllHandler);
        app.get("messages/{message_id}", this::messageRetrieveByIDHandler);
        app.delete("messages/{message_id}", this::messageDeleteByIdHandler);
        app.get("accounts/{account_id}/messages", this::messageRetrieveByUserIdHandler);
        app.patch("messages/{message_id}", this::messageUpdateByIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registrationHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body().toString(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body().toString(), Account.class);
        Account loginAccount = accountService.loginAccount(account);
        if (loginAccount == null) {
            context.status(401);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(loginAccount));
        }
    }

    private void messageCreationHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body().toString(), Message.class);
        Message addedMessage;
        try {
            addedMessage = messageService.addMessage(message);
            if (addedMessage == null) {
                context.status(400);
            } else {
                context.status(200);
                context.json(mapper.writeValueAsString(addedMessage));
            }
        } catch (SQLException e) {
            context.status(400);
        }
    }

    public void messageRetrieveAllHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        final List<Message> messages = messageService.getAllMessages();
        if (messages == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(messages));
        }

    }

    public void messageRetrieveByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        final Message message = messageService.getMessageById(messageId);
        if (message == null) {
            context.status(400);
        } else {
            context.status(200);
            if (message.getMessage_id() == 0 && message.getMessage_text().equals("")) {
                context.json("");
            } else {
                context.json(mapper.writeValueAsString(message));
            }

        }
    }

    public void messageDeleteByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        try {
            final Message message = messageService.removeMessageById(messageId);
            context.status(200);
            if (message == null) {
                context.json("");
            } else {
                context.json(mapper.writeValueAsString(message));
            }
        } catch (SQLException e) {
            System.out.println("Exception occurred while deleting message by ID : " + e.getMessage());
            e.printStackTrace();
            context.status(400);
        }
    }

    public void messageRetrieveByUserIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int userId = Integer.parseInt(context.pathParam("account_id"));
        try {
            final List<Message> messages = messageService.getAllMessagesByUserId(userId);
            context.status(200);
            context.json(mapper.writeValueAsString(messages));
        } catch (SQLException e) {
            System.out.println("Exception occurred while retrieving all message for user ID : " + e.getMessage());
            e.printStackTrace();
            context.status(400);
        }
    }

    public void messageUpdateByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));

        String messageText = context.bodyAsClass(Map.class).get("message_text").toString();
        System.out.println(messageText);
        try {
            final Message message = messageService.updateMessageTextByMessageId(messageId, messageText);

            if (message != null) {
                context.status(200);
                context.json(mapper.writeValueAsString(message));
            } else {
                context.status(400);
            }
        } catch (SQLException e) {
            System.out.println("Exception occurred while updating the message by ID " + e.getMessage());
            e.printStackTrace();
            context.status(400);
        }

    }

}