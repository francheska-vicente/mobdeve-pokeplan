package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;
import java.util.Date;

public class TaskDataHelper {
    public static ArrayList<Task> loadTaskData() {
        ArrayList<Task> data = new ArrayList<Task>();

        data.add(new Task("Study", 1, "School", new Date(), new Date(), "rawr"));
        data.add(new Task("Genshin Impact", 1, "Leisure", new Date(), new Date(), "rawr"));
        data.add(new Task("Bake", 1, "Hobby", new Date(), new Date(), "rawr"));
        data.add(new Task("Exercise", 1, "Health", new Date(), new Date(), "rawr"));

        return data;
    }
}
