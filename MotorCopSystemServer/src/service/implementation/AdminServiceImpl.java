package service.implementation;

import model.Admin;
import dao.AdminDAO;
import service.AdminService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class AdminServiceImpl extends UnicastRemoteObject implements AdminService {
    private final AdminDAO adminDAO;

    public AdminServiceImpl() throws RemoteException {
        super();
        this.adminDAO = new AdminDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    public Admin save(Admin admin) throws RemoteException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Check if email already exists
            Admin existingAdmin = (Admin) session.createQuery("FROM Admin WHERE email = :email")
                    .setParameter("email", admin.getEmail())
                    .uniqueResult();
            if (existingAdmin != null) {
                throw new RemoteException("Email already registered");
            }

            String otp = generateOTP();
            admin.setVerificationToken(otp);
            admin.setPassword(hashPassword(admin.getPassword()));
            session.save(admin);
            transaction.commit();

            // Send OTP email
            sendVerificationEmail(admin.getEmail(), otp);
            return admin;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RemoteException("Error saving admin", e);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit number between 100000 and 999999
        return String.valueOf(otp);
    }

    private void sendVerificationEmail(String email, String otp) throws RemoteException {
        String from = "abikundamugisha6@gmail.com"; // Replace with your Gmail address
        String password = "jrgo wdiz lfqt kuoi"; // Replace with your 16-character Gmail App Password
        String host = "smtp.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        javax.mail.Session mailSession = javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Email Verification");
            message.setText("Your verification OTP is: " + otp + "\nPlease use this OTP to verify your email address.");
            Transport.send(message);
            System.out.println("Verification email sent to " + email);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + email + ": " + e.getMessage());
            throw new RemoteException("Error sending verification email", e);
        }
    }

    @Override
    public Admin findById(int id) throws RemoteException {
        return adminDAO.findById(id);
    }

    @Override
    public List<Admin> findAll() throws RemoteException {
        return adminDAO.findAll();
    }

    @Override
    public Admin update(Admin admin) throws RemoteException {
        return adminDAO.update(admin);
    }

    @Override
    public Admin delete(Admin admin) throws RemoteException {
        return adminDAO.delete(admin);
    }

    @Override
    public Admin findByIdUsingHQL(int id) throws RemoteException {
        return adminDAO.findByIdUsingHQL(id);
    }

    @Override
    public Admin findByUsername(String username) throws RemoteException {
        return adminDAO.findByUsername(username);
    }

    @Override
    public Admin findByEmail(String email) throws RemoteException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Admin admin = (Admin) session.createQuery("FROM Admin a WHERE a.email = :email")
                    .setParameter("email", email)
                    .uniqueResult();
            return admin;
        } catch (Exception e) {
            throw new RemoteException("Error finding admin by email", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean verifyEmail(String token) throws RemoteException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Admin admin = (Admin) session.createQuery("FROM Admin a WHERE a.verificationToken = :token")
                    .setParameter("token", token)
                    .uniqueResult();
            if (admin != null) {
                admin.setVerified(true);
                admin.setVerificationToken(null);
                session.update(admin);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RemoteException("Error verifying email", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Admin login(String email, String password) throws RemoteException {
        Admin admin = findByEmail(email);
        if (admin != null && admin.isVerified() && verifyPassword(password, admin.getPassword())) {
            return admin;
        }
        return null;
    }

    private String hashPassword(String password) {
        // Simple hashing for demo; use BCrypt in production
        return Integer.toHexString(password.hashCode());
    }

    private boolean verifyPassword(String inputPassword, String storedPassword) {
        return hashPassword(inputPassword).equals(storedPassword);
    }
}