package service;

import model.AccountTransaction;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AccountTransactionService extends Remote {
    AccountTransaction save(AccountTransaction accountTransaction) throws RemoteException;
    AccountTransaction findById(int transactionId) throws RemoteException;
    List<AccountTransaction> findAll() throws RemoteException;
    AccountTransaction update(AccountTransaction accountTransaction) throws RemoteException;
    AccountTransaction delete(AccountTransaction accountTransaction) throws RemoteException;
    AccountTransaction findByIdUsingHQL(int transactionId) throws RemoteException;
    List<AccountTransaction> findBySavingsAccountId(int accountId) throws RemoteException;
}