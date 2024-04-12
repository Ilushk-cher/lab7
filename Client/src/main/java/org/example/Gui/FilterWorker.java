package org.example.Gui;

import org.example.CollectionModel.HumanBeing;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;

public class FilterWorker {
    private final LinkedHashMap<Integer, Predicate<HumanBeing>> predicates = new LinkedHashMap<>();

    public void addPredicate(int row, Predicate<HumanBeing> predicate) {
        predicates.put(row, predicate);
    }

    public Predicate<HumanBeing> getPredicate() {
        return predicates.values().stream().reduce(x -> true, Predicate::and);
    }

    public void clearPredicates() {
        predicates.clear();
    }

    public void parsePredicate(int row, List<?> values) {
        switch (row) {
            case 0 -> addPredicate(0, (o) -> values.contains(o.getId()));
            case 1 -> addPredicate(1, (o) -> values.contains(o.getName()));
            case 2 -> addPredicate(2, (o) -> values.contains(o.getCoordinates()));
            case 3 -> addPredicate(3, (o) -> values.contains(o.getCreationDate()));
            case 4 -> addPredicate(4, (o) -> values.contains(o.getRealHero()));
            case 5 -> addPredicate(5, (o) -> values.contains(o.getHasToothpick()));
            case 6 -> addPredicate(6, (o) -> values.contains(o.getImpactSpeed()));
            case 7 -> addPredicate(7, (o) -> values.contains(o.getWeaponType()));
            case 8 -> addPredicate(8, (o) -> values.contains(o.getMood()));
            case 9 -> addPredicate(9, (o) -> values.contains(o.getCar()));
            case 10 -> addPredicate(10, (o) -> values.contains(o.getUserLogin()));
        }
    }
}
