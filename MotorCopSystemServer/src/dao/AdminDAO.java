package dao;

import model.Admin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class AdminDAO {
    private final SessionFactory sessionFactory;

    public AdminDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Admin save(Admin admin) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(admin);
            tx.commit();
            return admin;
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

    public Admin findById(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Admin admin = (Admin) session.get(Admin.class, id);
            return admin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Admin> findAll() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Admin> admins = session.createQuery("FROM Admin").list();
            return admins;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Admin update(Admin admin) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.update(admin);
            tx.commit();
            return admin;
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

    public Admin delete(Admin admin) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(admin);
            tx.commit();
            return admin;
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

    public Admin findByIdUsingHQL(int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Admin admin = (Admin) session.createQuery("FROM Admin a WHERE a.id = :id")
                    .setParameter("id", id)
                    .uniqueResult();
            return admin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Admin findByUsername(String username) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Admin admin = (Admin) session.createQuery("FROM Admin a WHERE a.username = :username")
                    .setParameter("username", username)
                    .uniqueResult();
            return admin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Admin findByEmail(String email) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Admin admin = (Admin) session.createQuery("FROM Admin a WHERE a.email = :email")
                    .setParameter("email", email)
                    .uniqueResult();
            return admin;
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