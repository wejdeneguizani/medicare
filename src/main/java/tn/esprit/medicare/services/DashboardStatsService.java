package tn.esprit.medicare.services;

import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.entities.MesureSante;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardStatsService {

    public StatsSnapshot buildSnapshot(List<Habitude> habitudes, List<MesureSante> mesures) {
        StatsSnapshot snapshot = new StatsSnapshot();
        snapshot.totalHabits = habitudes.size();
        snapshot.totalMesures = mesures.size();
        snapshot.totalPatients = (int) mesures.stream().map(MesureSante::getUserId).distinct().count();
        if (snapshot.totalPatients == 0) {
            snapshot.totalPatients = (int) habitudes.stream().map(Habitude::getUserId).distinct().count();
        }

        snapshot.avgSteps = mesures.stream().mapToInt(MesureSante::getPas).average().orElse(0.0);

        Map<String, Long> typeCount = habitudes.stream()
                .collect(Collectors.groupingBy(h -> h.getType().name(), Collectors.counting()));
        snapshot.habitsByType = new LinkedHashMap<>(typeCount);

        Map<Integer, Integer> stepsByPatient = new HashMap<>();
        for (MesureSante mesure : mesures) {
            stepsByPatient.merge(mesure.getUserId(), mesure.getPas(), Integer::sum);
        }
        snapshot.stepsByPatient = stepsByPatient.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        snapshot.waterTrend = mesures.stream()
                .sorted(Comparator.comparing(MesureSante::getDateMesure))
                .collect(Collectors.groupingBy(
                        m -> m.getDateMesure().format(formatter),
                        LinkedHashMap::new,
                        Collectors.averagingDouble(MesureSante::getEauLitres)
                ));

        return snapshot;
    }

    public static class StatsSnapshot {
        public int totalPatients;
        public int totalHabits;
        public int totalMesures;
        public double avgSteps;
        public Map<String, Long> habitsByType = new LinkedHashMap<>();
        public Map<Integer, Integer> stepsByPatient = new LinkedHashMap<>();
        public Map<String, Double> waterTrend = new LinkedHashMap<>();

        public List<String> asDebugLines() {
            List<String> lines = new ArrayList<>();
            lines.add("patients=" + totalPatients);
            lines.add("habits=" + totalHabits);
            lines.add("mesures=" + totalMesures);
            lines.add("avgSteps=" + avgSteps);
            return lines;
        }
    }
}
