package dao;

import model.SavingsAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class SavingsAccountDAO {
    private final SessionFactory sessionFactory;

    public SavingsAccountDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Create a new savings account
    public SavingsAccount save(SavingsAccount savingsAccount) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(savingsAccount); // Cascades to transactions due to CascadeType.ALL
            tx.commit();
            return savingsAccount;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Read a savings account by ID
    public SavingsAccount findById(int accountId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            SavingsAccount savingsAccount = (SavingsAccount) session.get(SavingsAccount.class, accountId); // Explicit cast
            return savingsAccount;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Read all savings accounts
    public List<SavingsAccount> findAll() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<SavingsAccount> savingsAccounts = session.createQuery("FROM SavingsAccount").list();
            return savingsAccounts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Update a savings account
    public SavingsAccount update(SavingsAccount savingsAccount) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.update(savingsAccount); // Cascades to transactions
            tx.commit();
            return savingsAccount;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Delete a savings account
    public SavingsAccount delete(SavingsAccount savingsAccount) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(savingsAccount); // Cascades to transactions
            tx.commit();
            return savingsAccount;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Find savings account by ID using HQL
    public SavingsAccount findByIdUsingHQL(int accountId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            SavingsAccount savingsAccount = (SavingsAccount) session.createQuery("FROM SavingsAccount sa WHERE sa.accountId = :accountId")
                    .setParameter("accountId", accountId)
                    .uniqueResult();
            return savingsAccount;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Find savings account by motorcyclist ID
    public SavingsAccount findByMotorcyclistId(int motorcyclistId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            SavingsAccount savingsAccount = (SavingsAccount) session.createQuery("FROM SavingsAccount sa WHERE sa.motorcyclist.id = :motorcyclistId")
                    .setParameter("motorcyclistId", motorcyclistId)
                    .uniqueResult();
            return savingsAccount;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}