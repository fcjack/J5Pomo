package me.jackson.j5pomo.enums;

public enum ActivitiesEnum {
    NEW_TASK(0);

    private int id;

    private ActivitiesEnum(int id) {
        this.id = id;
    }

    public static ActivitiesEnum getActivityFromId(int id) {
        return ActivitiesEnum.values()[id];
    }

    public int getId() {
        return id;
    }
}
