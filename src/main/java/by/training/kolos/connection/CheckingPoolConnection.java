package by.training.kolos.connection;

import java.util.TimerTask;

public class CheckingPoolConnection extends TimerTask {

    @Override
    public void run() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.addConnectionIfNeeded();
    }
}
