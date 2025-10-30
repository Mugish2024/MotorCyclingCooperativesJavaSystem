package dao;

import model.Loan;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class LoanDAO {
    private final SessionFactory sessionFactory;

    public LoanDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Create a new loan
    public Loan save(Loan loan) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(loan);
            tx.commit();
            return loan;
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

    // Read a loan by ID
    public Loan findById(int loanId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Loan loan = (Loan) session.get(Loan.class, loanId); // Explicit cast
            return loan;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Read all loans
    public List<Loan> findAll() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Loan> loans = session.createQuery("FROM Loan").list();
            return loans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Update a loan
    public Loan update(Loan loan) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.update(loan);
            tx.commit();
            return loan;
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

    // Delete a loan
    public Loan delete(Loan loan) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(loan);
            tx.commit();
            return loan;
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

    // Find loan by ID using HQL
    public Loan findByIdUsingHQL(int loanId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Loan loan = (Loan) session.createQuery("FROM Loan l WHERE l.loanId = :loanId")
                    .setParameter("loanId", loanId)
                    .uniqueResult();
            return loan;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Find loans by motorcyclist ID
    public List<Loan> findByMotorcyclistId(int motorcyclistId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Loan> loans = session.createQuery("FROM Loan l WHERE l.motorcyclist.id = :motorcyclistId")
                    .setParameter("motorcyclistId", motorcyclistId)
                    .list();
            return loans;
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