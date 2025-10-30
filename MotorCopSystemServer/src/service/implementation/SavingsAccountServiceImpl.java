package service.implementation;

import dao.SavingsAccountDAO;
import model.SavingsAccount;
import service.SavingsAccountService;
import util.HibernateUtil;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class SavingsAccountServiceImpl extends UnicastRemoteObject implements SavingsAccountService {
    private final SavingsAccountDAO savingsAccountDAO;

    public SavingsAccountServiceImpl() throws RemoteException {
        this.savingsAccountDAO = new SavingsAccountDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    public SavingsAccount save(SavingsAccount savingsAccount) throws RemoteException {
        return savingsAccountDAO.save(savingsAccount);
    }

    @Override
    public SavingsAccount findById(int accountId) throws RemoteException {
        return savingsAccountDAO.findById(accountId);
    }

    @Override
    public List<SavingsAccount> findAll() throws RemoteException {
        return savingsAccountDAO.findAll();
    }

    @Override
    public SavingsAccount update(SavingsAccount savingsAccount) throws RemoteException {
        return savingsAccountDAO.update(savingsAccount);
    }

    @Override
    public SavingsAccount delete(SavingsAccount savingsAccount) throws RemoteException {
        return savingsAccountDAO.delete(savingsAccount);
    }

    @Override
    public SavingsAccount findByIdUsingHQL(int accountId) throws RemoteException {
        return savingsAccountDAO.findByIdUsingHQL(accountId);
    }

    @Override
    public SavingsAccount findByMotorcyclistId(int motorcyclistId) throws RemoteException {
        return savingsAccountDAO.findByMotorcyclistId(motorcyclistId);
    }
}