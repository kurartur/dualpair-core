package lt.dualpair.core.socionics;

import lt.dualpair.core.socionics.test.Choice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChoiceRepository extends CrudRepository<Choice, Integer> {

    @Query("from Choice c where c.code = ?1")
    Choice findByCode(String code);

}
