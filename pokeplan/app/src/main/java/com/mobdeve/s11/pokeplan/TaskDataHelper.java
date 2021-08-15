package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;
import java.util.Date;

public class TaskDataHelper {
    public static ArrayList<Task> loadTaskData() {
        ArrayList<Task> data = new ArrayList<Task>();




        return data;
    }

    public ArrayList<Task> getOngoingList ()
    {
        ArrayList<Task> data = new ArrayList<Task>();

        data.add(new Task("Study", 1, "School", new CustomDate(2021, 8, 20, 16, 00), new CustomDate(2021, 8, 8, 16, 00), "rawr"));
        data.add(new Task("Genshin Impact", 1, "Leisure", new CustomDate(), new CustomDate(), "rawr"));
        data.add(new Task("Study", 1, "School", new CustomDate(2021, 8, 20, 16, 00), new CustomDate(2021, 8, 8, 16, 00), "rawr"));
        data.add(new Task("Genshin Impact", 2, "Leisure", new CustomDate(), new CustomDate(), "rawr"));
        data.add(new Task("Study", 1, "School", new CustomDate(2021, 8, 20, 16, 00), new CustomDate(2021, 8, 8, 16, 00), "rawr"));
        data.add(new Task("Genshin Impact", 4, "Leisure", new CustomDate(), new CustomDate(), "rawr"));
        data.add(new Task("Study", 1, "School", new CustomDate(2021, 8, 20, 16, 00), new CustomDate(2021, 8, 8, 16, 00), "rawr"));
        data.add(new Task("Genshin Impact", 5, "Leisure", new CustomDate(), new CustomDate(), "rawr"));
        data.add(new Task("Study", 1, "School", new CustomDate(2021, 8, 20, 16, 00), new CustomDate(2021, 8, 8, 16, 00), "rawr"));
        data.add(new Task("Genshin Impact", 3, "Leisure", new CustomDate(), new CustomDate(), "rawr"));
        data.add(new Task("Study", 1, "School", new CustomDate(2021, 8, 20, 16, 00), new CustomDate(2021, 8, 8, 16, 00), "rawr"));
        data.add(new Task("Genshin Impact", 1, "Leisure", new CustomDate(), new CustomDate(), "rawr"));
        data.add(new Task("Study", 1, "School", new CustomDate(2021, 8, 20, 16, 00), new CustomDate(2021, 8, 8, 16, 00), "rawr"));
        data.add(new Task("Genshin Impact", 2, "Leisure", new CustomDate(), new CustomDate(), "rawr"));
        data.add(new Task("Study", 1, "School", new CustomDate(2021, 8, 20, 16, 00), new CustomDate(2021, 8, 8, 16, 00), "rawr"));
        data.add(new Task("Genshin Impact", 1, "Leisure", new CustomDate(), new CustomDate(), "rawr"));



        return data;
    }

    public ArrayList<Task> getCompletedList ()
    {
        ArrayList<Task> data = new ArrayList<Task>();

        data.add(new Task("Bake", 1, "Hobby", new CustomDate(), new CustomDate(), "rawr"));
        data.add(new Task("Exercise", 5, "Health", new CustomDate(2021, 10, 22, 19, 52), new CustomDate(2021, 10, 22, 19, 52), "rawr"));

        return data;
    }

}
