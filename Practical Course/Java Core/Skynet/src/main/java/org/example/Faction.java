package org.example;

class Faction {
    private final int[] parts = new int[Factory.TYPES_COUNT];

    public void addPart(int partIndex) {
        parts[partIndex]++;
    }

    public int getRobotsCount() {
        return Math.min(Math.min(Math.min(parts[0], parts[1]), parts[2]), parts[3]);
    }
}