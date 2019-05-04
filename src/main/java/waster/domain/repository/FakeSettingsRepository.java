package waster.domain.repository;

import waster.domain.entity.Setting;

import java.util.Arrays;
import java.util.List;

public class FakeSettingsRepository {
    private static final Long BASE_PREPARE_TIME = 15 * 60 * 1000L;
    //TODO REPLACE WORKING TIME TO WORKING SPEED
    public static List<Setting> settings = Arrays.asList(
            createSetting(-1L, 1.0, 0L),
            createSetting(-2L, 1.0, 0L),
            createSetting(274L, 1000L),
            createSetting(100L, 1200L),
            createSetting(115L, 1143L),
            createSetting(89L, 25_200_000.0, BASE_PREPARE_TIME, false),
            createSetting(90L, 5 * 60 * 1000.0, BASE_PREPARE_TIME, false),
            createSetting(105L, 5 * 60 * 1000L),
            createSetting(116L, 1200L),
            createSetting(88L, 3429L),
            createSetting(86L, 4000L),
            createSetting(91L, 2608L),
            createSetting(94L, 5 * 60 * 1000L),
            createSetting(211L, 5 * 60 * 1000L),
            createSetting(271L, 5 * 60 * 1000L),
            createSetting(266L, 5 * 60 * 1000L),
            createSetting(268L, 5 * 60 * 1000L),
            createSetting(269L, 5 * 60 * 1000L),
            createSetting(261L, 5 * 60 * 1000L),
            createSetting(262L, 5 * 60 * 1000L),
            createSetting(264L, 5 * 60 * 1000L),
            createSetting(265L, 5 * 60 * 1000L),
            createSetting(260L, 5 * 60 * 1000L),
            createSetting(259L, 5 * 60 * 1000L),
            createSetting(242L, 5 * 60 * 1000L),
            createSetting(243L, 5 * 60 * 1000L),
            createSetting(246L, 5 * 60 * 1000L),
            createSetting(247L, 5 * 60 * 1000L),
            createSetting(252L, 5 * 60 * 1000L),
            createSetting(253L, 5 * 60 * 1000L),
            createSetting(258L, 5 * 60 * 1000L)
    );

    private static Setting createSetting(Long id, Double workingSpeed) {
        return createSetting(id, workingSpeed, BASE_PREPARE_TIME);
    }

    private static Setting createSetting(Long id, Long workingSpeed) {
        return createSetting(id, new Double(workingSpeed), BASE_PREPARE_TIME);
    }

    private static Setting createSetting(Long id, Double workingSpeed, Long prepareTime) {
        return createSetting(id, workingSpeed, prepareTime, true);
    }

    private static Setting createSetting(Long id, Double workingSpeed, Long prepareTime, Boolean isDependOfTime) {
        Setting setting = new Setting();
        setting.setPrepareTime(prepareTime);
        setting.setWorkingSpeed(workingSpeed);
        setting.setId(id);
        setting.setTimeDependOnLength(isDependOfTime);
        return setting;
    }

    public Setting getSettingById(Long id) {
        return settings.stream().filter(x -> x.getId().equals(id)).findFirst().get();
    }

}
