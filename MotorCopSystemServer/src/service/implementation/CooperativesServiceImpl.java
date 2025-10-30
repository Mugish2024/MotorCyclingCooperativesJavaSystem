/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;



import dao.CooperativesDAO;
import model.Cooperatives;
import service.CooperativesService;
import util.HibernateUtil;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CooperativesServiceImpl extends UnicastRemoteObject implements CooperativesService {
    private final CooperativesDAO cooperativesDAO;

    public CooperativesServiceImpl() throws RemoteException {
        this.cooperativesDAO = new CooperativesDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    public Cooperatives save(Cooperatives cooperative) throws RemoteException {
        return cooperativesDAO.save(cooperative);
    }

    @Override
    public Cooperatives findById(int id) throws RemoteException {
        return cooperativesDAO.findById(id);
    }

    @Override
    public List<Cooperatives> findAll() throws RemoteException {
        return cooperativesDAO.findAll();
    }

    @Override
    public Cooperatives update(Cooperatives cooperative) throws RemoteException {
        return cooperativesDAO.update(cooperative);
    }

    @Override
    public Cooperatives delete(Cooperatives cooperative) throws RemoteException {
        return cooperativesDAO.delete(cooperative);
    }

    @Override
    public Cooperatives findByIdUsingHQL(int id) throws RemoteException {
        return cooperativesDAO.findByIdUsingHQL(id);
    }
}