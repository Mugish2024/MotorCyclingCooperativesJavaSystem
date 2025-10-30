package dao;

import model.Cooperatives;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class CooperativesDAO {
    private final SessionFactory sessionFactory;

    public CooperativesDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Create a new cooperative
    public Cooperatives save(Cooperatives cooperative) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(cooperative); // Cascades to motorcyclists due to CascadeType.ALL
            tx.commit();
            return cooperative;
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

    // Read a cooperative by ID
    public Cooperatives findById(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Cooperatives cooperative = (Cooperatives) session.get(Cooperatives.class, id); // Explicit cast
            return cooperative;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Read all cooperatives
    public List<Cooperatives> findAll() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Cooperatives> cooperatives = session.createQuery("FROM Cooperatives").list();
            return cooperatives;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Update a cooperative
    public Cooperatives update(Cooperatives cooperative) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.update(cooperative); // Cascades to motorcyclists
            tx.commit();
            return cooperative;
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

    // Delete a cooperative
    public Cooperatives delete(Cooperatives cooperative) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(cooperative); // Cascades to motorcyclists
            tx.commit();
            return cooperative;
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

    // Find cooperative by ID using HQL
    public Cooperatives findByIdUsingHQL(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Cooperatives cooperative = (Cooperatives) session.createQuery("FROM Cooperatives c WHERE c.id = :id")
                    .setParameter("id", id)
                    .uniqueResult();
            return cooperative;
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