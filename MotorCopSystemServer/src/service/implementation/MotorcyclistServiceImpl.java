package service.implementation;

import dao.MotorcyclistDAO;
import model.Motorcyclist;
import service.MotorcyclistService;
import util.HibernateUtil;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MotorcyclistServiceImpl extends UnicastRemoteObject implements MotorcyclistService {
    private final MotorcyclistDAO motorcyclistDAO;

    public MotorcyclistServiceImpl() throws RemoteException {
        this.motorcyclistDAO = new MotorcyclistDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    public Motorcyclist save(Motorcyclist motorcyclist) throws RemoteException {
        return motorcyclistDAO.save(motorcyclist);
    }

    @Override
    public Motorcyclist findById(int id) throws RemoteException {
        return motorcyclistDAO.findById(id);
    }

    @Override
    public List<Motorcyclist> findAll() throws RemoteException {
        return motorcyclistDAO.findAll();
    }

    @Override
    public Motorcyclist update(Motorcyclist motorcyclist) throws RemoteException {
        return motorcyclistDAO.update(motorcyclist);
    }

    @Override
    public Motorcyclist delete(Motorcyclist motorcyclist) throws RemoteException {
        return motorcyclistDAO.delete(motorcyclist);
    }

    @Override
    public Motorcyclist findByIdUsingHQL(int id) throws RemoteException {
        return motorcyclistDAO.findByIdUsingHQL(id);
    }

    @Override
    public List<Motorcyclist> findByCooperativeId(int cooperativeId) throws RemoteException {
        return motorcyclistDAO.findByCooperativeId(cooperativeId);
    }
}