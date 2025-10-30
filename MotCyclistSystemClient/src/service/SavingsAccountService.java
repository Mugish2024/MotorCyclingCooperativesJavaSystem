package service;

import model.SavingsAccount;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SavingsAccountService extends Remote {
    SavingsAccount save(SavingsAccount savingsAccount) throws RemoteException;
    SavingsAccount findById(int accountId) throws RemoteException;
    List<SavingsAccount> findAll() throws RemoteException;
    SavingsAccount update(SavingsAccount savingsAccount) throws RemoteException;
    SavingsAccount delete(SavingsAccount savingsAccount) throws RemoteException;
    SavingsAccount findByIdUsingHQL(int accountId) throws RemoteException;
    SavingsAccount findByMotorcyclistId(int motorcyclistId) throws RemoteException;
}