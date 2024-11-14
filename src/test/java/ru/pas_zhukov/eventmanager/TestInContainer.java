package ru.pas_zhukov.eventmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@AutoConfigureMockMvc
@SpringBootTest
public class TestInContainer {
    private static volatile boolean isSharedSetupDone = false;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper jacksonObjectMapper;


    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:15.3")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("12345");
    // static блок выполняется один раз при загрузке класса в память
    // проверка с volatile флагом нужна для того, чтобы исключить повторные создания контейнеров в разных потоках
    static {
        if (!isSharedSetupDone) {
            POSTGRES_CONTAINER.start();
            isSharedSetupDone = true;
        }
    }

    // поскольку контейнер запускается на рандомном порте, нам нужно прокинуть этот порт в application.properties
    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("test.postgres.port", POSTGRES_CONTAINER::getFirstMappedPort);
    }

    // останавливаем контейнер когда останавливается контекст
    @EventListener
    public void stopContainer(ContextStoppedEvent e) {
        POSTGRES_CONTAINER.stop();
    }

}
