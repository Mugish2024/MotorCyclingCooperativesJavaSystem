package service.implementation;

import dao.LoanDAO;
import model.Loan;
import service.LoanService;
import util.HibernateUtil;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LoanServiceImpl extends UnicastRemoteObject implements LoanService {
    private final LoanDAO loanDAO;

    public LoanServiceImpl() throws RemoteException {
        this.loanDAO = new LoanDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    public Loan save(Loan loan) throws RemoteException {
        return loanDAO.save(loan);
    }

    @Override
    public Loan findById(int loanId) throws RemoteException {
        return loanDAO.findById(loanId);
    }

    @Override
    public List<Loan> findAll() throws RemoteException {
        return loanDAO.findAll();
    }

    @Override
    public Loan update(Loan loan) throws RemoteException {
        return loanDAO.update(loan);
    }

    @Override
    public Loan delete(Loan loan) throws RemoteException {
        return loanDAO.delete(loan);
    }

    @Override
    public Loan findByIdUsingHQL(int loanId) throws RemoteException {
        return loanDAO.findByIdUsingHQL(loanId);
    }

    @Override
    public List<Loan> findByMotorcyclistId(int motorcyclistId) throws RemoteException {
        return loanDAO.findByMotorcyclistId(motorcyclistId);
    }
}