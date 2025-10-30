package service;

import model.Motorcyclist;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MotorcyclistService extends Remote {
    Motorcyclist save(Motorcyclist motorcyclist) throws RemoteException;
    Motorcyclist findById(int id) throws RemoteException;
    List<Motorcyclist> findAll() throws RemoteException;
    Motorcyclist update(Motorcyclist motorcyclist) throws RemoteException;
    Motorcyclist delete(Motorcyclist motorcyclist) throws RemoteException;
    Motorcyclist findByIdUsingHQL(int id) throws RemoteException;
    List<Motorcyclist> findByCooperativeId(int cooperativeId) throws RemoteException;
}