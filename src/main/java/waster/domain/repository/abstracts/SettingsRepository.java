package waster.domain.repository.abstracts;

import org.springframework.data.repository.CrudRepository;
import waster.domain.entity.Setting;

public interface SettingsRepository  extends CrudRepository<Setting,Long> {
}
