package pet.listener;

import pet.service.SessionService;
import pet.util.PersistenceUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

@WebListener
public class SessionCleanupListener implements ServletContextListener {
    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer = new Timer(true);
        SessionService sessionService = new SessionService();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sessionService.deleteExpiredSessions();
            }
        };
        timer.schedule(task, 0, 60 * 60 * 1000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (timer != null) {
            timer.cancel();
        }
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();

        PersistenceUtil.closeEntityManager();
    }
}




