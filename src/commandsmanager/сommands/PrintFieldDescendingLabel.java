package commandsmanager.сommands;

import commandsmanager.Command;
import data.ClassesManager;
import dataclasses.MusicBand;
import termenalmanager.Colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

public class PrintFieldDescendingLabel extends Command {
    @Override
    public void execute() {

        ClassesManager classesManager = ClassesManager.getInstance();
        Hashtable<Integer, MusicBand> map = classesManager.getMap();
        ArrayList<Integer> labels = new ArrayList<>();
        for (int key : map.keySet()) {
            labels.add(map.get(key).getLabel().getBands());
        }
        Collections.sort(labels, Collections.reverseOrder());
        print_label(labels);


    }

    public void print_label(ArrayList<Integer> labels) {
        StringBuilder builder = new StringBuilder();
        builder.append("Labels: ");
        for (int i : labels) {
            builder.append(i).append(" ");
        }
        System.out.println();
        System.out.println(builder);
        System.out.println();
    }

    @Override
    public void execute(String value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(String value1, MusicBand value2) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void execute(MusicBand value1) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public String commandInfo() {
        return "вывести значения поля label всех элементов в порядке убывания";
    }
}
