package by.training.kolos.connection;

import java.util.TimerTask;

/**
 * Класс для организации контроля за количеством соединений
 *
 * @author Колос Марина
 */
public class CheckingPoolConnection extends TimerTask {

    /**
     * Метод по выполнению отдельным потоком проверки количества соединения
     */
    @Override
    public void run() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.addConnectionIfNeeded();
    }
}
