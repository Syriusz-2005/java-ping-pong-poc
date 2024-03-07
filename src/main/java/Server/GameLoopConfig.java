package Server;

public record GameLoopConfig(int simulationStepsPerSecond, int simulationStepsToPacketsRatio, int preparationTimeInTicks) {
}
