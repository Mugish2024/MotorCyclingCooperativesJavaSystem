package service;

import model.Cooperatives;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CooperativesService extends Remote {
    Cooperatives save(Cooperatives cooperative) throws RemoteException;
    Cooperatives findById(int id) throws RemoteException;
    List<Cooperatives> findAll() throws RemoteException;
    Cooperatives update(Cooperatives cooperative) throws RemoteException;
    Cooperatives delete(Cooperatives cooperative) throws RemoteException;
    Cooperatives findByIdUsingHQL(int id) throws RemoteException;
}