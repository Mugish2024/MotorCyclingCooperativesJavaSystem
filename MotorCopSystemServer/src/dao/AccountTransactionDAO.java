package dao;

import model.AccountTransaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class AccountTransactionDAO {
    private final SessionFactory sessionFactory;

    public AccountTransactionDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Create a new account transaction
    public AccountTransaction save(AccountTransaction accountTransaction) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(accountTransaction);
            tx.commit();
            return accountTransaction;
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

    // Read an account transaction by ID
    public AccountTransaction findById(int transactionId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            AccountTransaction accountTransaction = (AccountTransaction) session.get(AccountTransaction.class, transactionId); // Explicit cast
            return accountTransaction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Read all account transactions
    public List<AccountTransaction> findAll() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<AccountTransaction> accountTransactions = session.createQuery("FROM AccountTransaction").list();
            return accountTransactions;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Update an account transaction
    public AccountTransaction update(AccountTransaction accountTransaction) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.update(accountTransaction);
            tx.commit();
            return accountTransaction;
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

    // Delete an account transaction
    public AccountTransaction delete(AccountTransaction accountTransaction) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(accountTransaction);
            tx.commit();
            return accountTransaction;
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

    // Find account transaction by ID using HQL
    public AccountTransaction findByIdUsingHQL(int transactionId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            AccountTransaction accountTransaction = (AccountTransaction) session.createQuery("FROM AccountTransaction t WHERE t.transactionId = :transactionId")
                    .setParameter("transactionId", transactionId)
                    .uniqueResult();
            return accountTransaction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Find account transactions by savings account ID
    public List<AccountTransaction> findBySavingsAccountId(int accountId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<AccountTransaction> accountTransactions = session.createQuery("FROM AccountTransaction t WHERE t.savingsAccount.accountId = :accountId")
                    .setParameter("accountId", accountId)
                    .list();
            return accountTransactions;
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