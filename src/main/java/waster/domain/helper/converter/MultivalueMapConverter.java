package waster.domain.helper.converter;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import waster.domain.entity.Setting;

import javax.persistence.AttributeConverter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MultivalueMapConverter implements AttributeConverter<MultiValueMap<Long, Setting>, String> {

    @Override
    public String convertToDatabaseColumn(MultiValueMap<Long, Setting> longSettingMultiValueMap) {
        StringBuilder result = new StringBuilder();
        Set<Long> longSet = longSettingMultiValueMap.keySet();
        for (Long key : longSet) {
            List<Setting> settings = longSettingMultiValueMap.get(key);
            result.append(key).append(":").append(settings.stream().map(Setting::getId).map(Objects::toString).collect(Collectors.joining(","))).append(";");
        }
        return result.toString();
    }

    @Override
    public MultiValueMap<Long, Setting> convertToEntityAttribute(String s) {

        String[] records = s.split(";");
        MultiValueMap map = new LinkedMultiValueMap();
        for (String record : records) {
            Long key = Long.valueOf(record.split(":")[0]);
            String[] settingsId = record.split(":")[1].split(",");
            for (String settingId : settingsId) {
                Long id = Long.valueOf(settingId);
                Setting setting = Setting.builder().id(id).build(); //settingsRepository.findById(id).get();
                map.add(key, setting);
            }
        }

        return map;
    }
}
