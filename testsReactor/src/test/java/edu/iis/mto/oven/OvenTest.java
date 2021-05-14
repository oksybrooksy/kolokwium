package edu.iis.mto.oven;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class OvenTest {
    private Oven oven;
    @Mock
    private HeatingModule heatingModule;
    @Mock
    private Fan fan;

    @BeforeEach
    void setup(){
        oven = new Oven(heatingModule, fan);
    }

    @Test
    void itCompiles() {
        MatcherAssert.assertThat(true, equalTo(true));
    }

    @Test
    void RunStageShouldThrowOvenException() throws HeatingException {
        HeatType heat = HeatType.THERMO_CIRCULATION;
        int stageTime = 50;
        int targetTemp = 180;
        int initTemp = 0;

        ProgramStage programStages = ProgramStage.builder().withTargetTemp(targetTemp)
                .withStageTime(stageTime)
                .withHeat(heat)
                .build();
        List<ProgramStage> programStagesList = List.of(programStages);

        BakingProgram bakingProgram = BakingProgram.builder().withInitialTemp(initTemp)
                .withStages(programStagesList).build();

        HeatingSettings heatingSettings = HeatingSettings.builder().withTargetTemp(targetTemp)
                .withTimeInMinutes(stageTime).build();

        doThrow(HeatingException.class).when(heatingModule).termalCircuit(heatingSettings);
        assertThrows(OvenException.class, () ->  oven.start(bakingProgram));
    }

    @Test
    void runHeatingProgramThrowOvenException() throws HeatingException {
        HeatType heat = HeatType.GRILL;
        int stageTime = 50;
        int targetTemp = 180;
        int initTemp = 0;

        ProgramStage programStages = ProgramStage.builder().withTargetTemp(targetTemp)
                .withStageTime(stageTime)
                .withHeat(heat)
                .build();
        List<ProgramStage> programStagesList = List.of(programStages);

        BakingProgram bakingProgram = BakingProgram.builder().withInitialTemp(initTemp)
                .withStages(programStagesList).build();

        HeatingSettings heatingSettings = HeatingSettings.builder().withTargetTemp(targetTemp)
                .withTimeInMinutes(stageTime).build();

        doThrow(HeatingException.class).when(heatingModule).grill(heatingSettings);
        assertThrows(OvenException.class, () ->  oven.start(bakingProgram));
    }

    @Test
    void ZeroInvocationsOfHeatingModule(){
        BakingProgram bakingProgram = BakingProgram.builder().build();
        oven.start(bakingProgram);

        verify(heatingModule, times(0)).heater(HeatingSettings.builder()
                .withTimeInMinutes(10).withTargetTemp(bakingProgram.getInitialTemp())
                .build());
    }

}
