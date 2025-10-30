package service.implementation;

import dao.AccountTransactionDAO;
import model.AccountTransaction;
import service.AccountTransactionService;
import util.HibernateUtil;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AccountTransactionServiceImpl extends UnicastRemoteObject implements AccountTransactionService {
    private final AccountTransactionDAO accountTransactionDAO;

    public AccountTransactionServiceImpl() throws RemoteException {
        this.accountTransactionDAO = new AccountTransactionDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    public AccountTransaction save(AccountTransaction accountTransaction) throws RemoteException {
        return accountTransactionDAO.save(accountTransaction);
    }

    @Override
    public AccountTransaction findById(int transactionId) throws RemoteException {
        return accountTransactionDAO.findById(transactionId);
    }

    @Override
    public List<AccountTransaction> findAll() throws RemoteException {
        return accountTransactionDAO.findAll();
    }

    @Override
    public AccountTransaction update(AccountTransaction accountTransaction) throws RemoteException {
        return accountTransactionDAO.update(accountTransaction);
    }

    @Override
    public AccountTransaction delete(AccountTransaction accountTransaction) throws RemoteException {
        return accountTransactionDAO.delete(accountTransaction);
    }

    @Override
    public AccountTransaction findByIdUsingHQL(int transactionId) throws RemoteException {
        return accountTransactionDAO.findByIdUsingHQL(transactionId);
    }

    @Override
    public List<AccountTransaction> findBySavingsAccountId(int accountId) throws RemoteException {
        return accountTransactionDAO.findBySavingsAccountId(accountId);
    }
}