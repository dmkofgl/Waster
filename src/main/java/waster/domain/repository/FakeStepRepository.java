package waster.domain.repository;

import waster.domain.entity.Machine;
import waster.domain.entity.Setting;
import waster.domain.entity.Step;

import java.util.Arrays;
import java.util.List;

public class FakeStepRepository {
    private static FakeMachineRepository machineRepository = new FakeMachineRepository();
    private static FakeSettingsRepository settingsRepository = new FakeSettingsRepository();

    private static List<Step> steps = Arrays.asList(
            createStep(-1L, "ИСТОК", machineRepository.getMachinesById(-1L), settingsRepository.getSettingById(-1L)),
            createStep(-2L, "ВТОК", machineRepository.getMachinesById(-2L), settingsRepository.getSettingById(-2L)),
            //art 00 C65-KB
            createStep(0L, "Опаливание", machineRepository.getMachinesById(82L), settingsRepository.getSettingById(274L)),
            createStep(1L, "", machineRepository.getMachinesById(20L), settingsRepository.getSettingById(100L)),
            createStep(2L, "", machineRepository.getMachinesById(21L), settingsRepository.getSettingById(115L)),
            createStep(3L, "", machineRepository.getMachinesById(79L), settingsRepository.getSettingById(89L)),
            createStep(4L, "", machineRepository.getMachinesById(78L), settingsRepository.getSettingById(105L)),
            createStep(5L, "", machineRepository.getMachinesById(20L), settingsRepository.getSettingById(116L)),
            createStep(6L, "", machineRepository.getMachinesById(56L), settingsRepository.getSettingById(90L)),
            createStep(7L, "", machineRepository.getMachinesById(61L), settingsRepository.getSettingById(88L)),
            createStep(8L, "", machineRepository.getMachinesById(10L), settingsRepository.getSettingById(86L)),
            createStep(9L, "", machineRepository.getMachinesById(44L), settingsRepository.getSettingById(91L)),
            createStep(10L, "", machineRepository.getMachinesById(64L), settingsRepository.getSettingById(94L)),
            createStep(11L, "", machineRepository.getMachinesById(45L), settingsRepository.getSettingById(211L)),
            //art 08C6-KB
            createStep(12L, "", machineRepository.getMachinesById(30L), settingsRepository.getSettingById(271L)),
            createStep(13L, "", machineRepository.getMachinesById(14L), settingsRepository.getSettingById(266L)),
            createStep(14L, "", machineRepository.getMachinesById(14L), settingsRepository.getSettingById(268L)),
            //TODO WARRING stap 45
            createStep(15L, "", machineRepository.getMachinesById(45L), settingsRepository.getSettingById(269L)),
            //
            createStep(16L, "", machineRepository.getMachinesById(30L), settingsRepository.getSettingById(260L)),
            createStep(17L, "", machineRepository.getMachinesById(30L), settingsRepository.getSettingById(261L)),
            createStep(18L, "", machineRepository.getMachinesById(30L), settingsRepository.getSettingById(262L)),
            createStep(19L, "", machineRepository.getMachinesById(30L), settingsRepository.getSettingById(264L)),
            createStep(20L, "", machineRepository.getMachinesById(30L), settingsRepository.getSettingById(265L)),
            //art 4C5-KB
            createStep(21L, "", machineRepository.getMachinesById(82L), settingsRepository.getSettingById(259L)),
            createStep(22L, "", machineRepository.getMachinesById(20L), settingsRepository.getSettingById(242L)),
            createStep(23L, "", machineRepository.getMachinesById(78L), settingsRepository.getSettingById(243L)),
            createStep(24L, "", machineRepository.getMachinesById(22L), settingsRepository.getSettingById(246L)),
            createStep(25L, "", machineRepository.getMachinesById(76L), settingsRepository.getSettingById(247L)),
            createStep(26L, "", machineRepository.getMachinesById(10L), settingsRepository.getSettingById(252L)),
            createStep(27L, "", machineRepository.getMachinesById(44L), settingsRepository.getSettingById(253L)),
            createStep(28L, "", machineRepository.getMachinesById(45L), settingsRepository.getSettingById(258L))

    );

    private static Step createStep(Long id, String name, Machine machine, Setting setting) {
        return Step.builder()
                .id(id)
                .name(name)
                .machine(machine)
                .setting(setting)
                .build();
    }

    public List<Step> getSteps() {
        return steps;
    }

    public Step getById(Long id) {
        return steps.stream().filter(x -> x.getId().equals(id)).findFirst().get();
    }
}
