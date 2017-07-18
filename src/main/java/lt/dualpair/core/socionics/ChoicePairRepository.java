package lt.dualpair.core.socionics;

import lt.dualpair.core.socionics.test.ChoicePair;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ChoicePairRepository extends Repository<ChoicePair, Integer> {

    Optional<ChoicePair> findOne(Integer id);

}
