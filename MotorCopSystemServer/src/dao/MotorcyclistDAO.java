package dao;

import model.Motorcyclist;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class MotorcyclistDAO {
    private final SessionFactory sessionFactory;

    public MotorcyclistDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Create a new motorcyclist
    public Motorcyclist save(Motorcyclist motorcyclist) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(motorcyclist); // Cascades to savingsAccount and loans due to CascadeType.ALL
            tx.commit();
            return motorcyclist;
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

    // Read a motorcyclist by ID
    public Motorcyclist findById(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Motorcyclist motorcyclist = (Motorcyclist) session.get(Motorcyclist.class, id); // Explicit cast
            return motorcyclist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Read all motorcyclists
    public List<Motorcyclist> findAll() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Motorcyclist> motorcyclists = session.createQuery("FROM Motorcyclist").list();
            return motorcyclists;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Update a motorcyclist
    public Motorcyclist update(Motorcyclist motorcyclist) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.update(motorcyclist); // Cascades to savingsAccount and loans
            tx.commit();
            return motorcyclist;
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

    // Delete a motorcyclist
    public Motorcyclist delete(Motorcyclist motorcyclist) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(motorcyclist); // Cascades to savingsAccount and loans
            tx.commit();
            return motorcyclist;
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

    // Find motorcyclist by ID using HQL
    public Motorcyclist findByIdUsingHQL(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Motorcyclist motorcyclist = (Motorcyclist) session.createQuery("FROM Motorcyclist m WHERE m.id = :id")
                    .setParameter("id", id)
                    .uniqueResult();
            return motorcyclist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    // Find motorcyclists by cooperative ID
    public List<Motorcyclist> findByCooperativeId(int cooperativeId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Motorcyclist> motorcyclists = session.createQuery("FROM Motorcyclist m WHERE m.cooperative.id = :cooperativeId")
                    .setParameter("cooperativeId", cooperativeId)
                    .list();
            return motorcyclists;
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