package by.training.kolos.command.impl;

import by.training.kolos.connection.ConnectionPool;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseCommandTest {
    final Long USER_ID = 1L;
    final Long PHOTO_ID = 1L;

    @BeforeSuite
    public void initConnectionPoolTest() {
        ConnectionPool.getInstanceTest();
    }

    @AfterSuite
    public void destroyPoolTest() {
        ConnectionPool.getInstanceTest().destroyPool();
    }
}
