import com.xiaodao.GenericEntity;
import com.xiaodao.GenericEntityRepository;
import com.xiaodao.WebMvcApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = WebMvcApplication.class)
public class TestTransactionTest {

    @Value("${key1:}")
    private String key;

    @Test
    public void test1() {
        System.out.println(key);
    }

    @Autowired
    private GenericEntityRepository genericEntityRepository;

    @Test
    @Transactional
    public void givenGenericEntityRepository_whenSaveAndRetreiveEntity_thenOK() {
        GenericEntity genericEntity = genericEntityRepository.save(new GenericEntity("test"));
        GenericEntity foundEntity = genericEntityRepository.getById(genericEntity.getId());

        assertNotNull(foundEntity);
        assertEquals(genericEntity.getValue(), foundEntity.getValue());
    }
}
