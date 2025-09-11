package co.com.pragma.config;

import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import co.com.pragma.model.application.gateways.SenderGateway;
import co.com.pragma.model.application.gateways.StatusRepository;
import co.com.pragma.model.application.gateways.UserRestGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = UseCasesConfig.class)
class UseCasesConfigTest {

  @Autowired
  private ApplicationContext context;

  @MockitoBean
  private ApplicationRepository applicationRepository;
  @MockitoBean
  private LoanTypeRepository loanTypeRepository;
  @MockitoBean
  private StatusRepository statusRepository;
  @MockitoBean
  private SenderGateway senderGateway;
  @MockitoBean
  private UserRestGateway userRestGateway;

  @Test
  void testUseCaseBeansExist() {
    String[] beanNames = context.getBeanDefinitionNames();
    boolean useCaseBeanFound = false;
    for (String beanName : beanNames) {
      if (beanName.endsWith("UseCase")) {
        useCaseBeanFound = true;
        assertNotNull(context.getBean(beanName));
      }
    }
    assertTrue(useCaseBeanFound, "No UseCase beans found in the context");
  }
}
