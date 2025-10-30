package service;

import model.Loan;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LoanService extends Remote {
    Loan save(Loan loan) throws RemoteException;
    Loan findById(int loanId) throws RemoteException;
    List<Loan> findAll() throws RemoteException;
    Loan update(Loan loan) throws RemoteException;
    Loan delete(Loan loan) throws RemoteException;
    Loan findByIdUsingHQL(int loanId) throws RemoteException;
    List<Loan> findByMotorcyclistId(int motorcyclistId) throws RemoteException;
}