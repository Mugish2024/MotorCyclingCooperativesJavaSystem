package controller;

import service.implementation.CooperativesServiceImpl;
import service.implementation.MotorcyclistServiceImpl;
import service.implementation.SavingsAccountServiceImpl;
import service.implementation.AccountTransactionServiceImpl;
import service.implementation.LoanServiceImpl;
import service.implementation.AdminServiceImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerController {
    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Registry registry = LocateRegistry.createRegistry(2325);
            registry.rebind("CooperativesService", new CooperativesServiceImpl());
            registry.rebind("MotorcyclistService", new MotorcyclistServiceImpl());
            registry.rebind("SavingsAccountService", new SavingsAccountServiceImpl());
            registry.rebind("AccountTransactionService", new AccountTransactionServiceImpl());
            registry.rebind("LoanService", new LoanServiceImpl());
            registry.rebind("AdminService", new AdminServiceImpl());
            System.out.println("Server is running on 127.0.0.1:2325");
        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}