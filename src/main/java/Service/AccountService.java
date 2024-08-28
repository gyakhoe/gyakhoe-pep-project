package Service;

import java.sql.SQLException;



import DAO.AccountDAO;
import Model.Account;

public class AccountService {


    private AccountDAO accountDAO;


    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) throws SQLException {
        if(account.getUsername().isEmpty() || account.password.length() <= 4) {
            System.out.println("Username is empty or password is short");
            return null;
        }
        if(accountDAO.selectAccountByUsername(account.getUsername()) != null) {
            System.out.println("Username already exist "+account.getUsername());
            return null;
        }
        return accountDAO.insertAccount(account);
    
    }

    public Account loginAccount(Account account) throws SQLException {
        Account accountFromDatabase = accountDAO.selectAccountByUsername(account.getUsername());
       
        if(accountFromDatabase != null && 
            accountFromDatabase.getUsername().equals(account.getUsername()) && 
            accountFromDatabase.getPassword().equals(account.getPassword())) {
            return accountFromDatabase;
        }
        return null;
        
        
    
        
    }
    
}
