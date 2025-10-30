package service;

import model.Admin;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AdminService extends Remote {
    Admin save(Admin admin) throws RemoteException;
    Admin findById(int id) throws RemoteException;
    List<Admin> findAll() throws RemoteException;
    Admin update(Admin admin) throws RemoteException;
    Admin delete(Admin admin) throws RemoteException;
    Admin findByIdUsingHQL(int id) throws RemoteException;
    Admin findByUsername(String username) throws RemoteException;
    Admin findByEmail(String email) throws RemoteException;  // Must be present
    boolean verifyEmail(String token) throws RemoteException; // Must be present
    Admin login(String email, String password) throws RemoteException; // Must be present
}