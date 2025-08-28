package co.com.pragma;

import co.com.pragma.model.application.gateways.ApplicationRepository;
import co.com.pragma.model.application.gateways.LoanTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class MainApplicationTest {

    @MockitoBean
    private ApplicationRepository applicationRepository;

    @MockitoBean
    private LoanTypeRepository loanTypeRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void main() {
        MainApplication.main(new String[]{});
    }

}
