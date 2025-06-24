package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Faction world = new Faction();
        Faction wednesday = new Faction();

        Factory factory = new Factory();
        Thread factoryThread = new Thread(factory);

        Thread worldThread = new Thread(() -> {
            while (factory.isRunning()) {
                factory.collectParts(world);
            }
        });

        Thread wednesdayThread = new Thread(() -> {
            while (factory.isRunning()) {
                factory.collectParts(wednesday);
            }
        });

        factoryThread.start();
        worldThread.start();
        wednesdayThread.start();
        factoryThread.join();

        worldThread.join();
        wednesdayThread.join();

        int worldArmy = world.getRobotsCount();
        int wednesdayArmy = wednesday.getRobotsCount();
        System.out.println("World's army: " + worldArmy);
        System.out.println("Wednesday's army: " + wednesdayArmy);

        if (worldArmy > wednesdayArmy) System.out.println("World won!");
        else if (wednesdayArmy > worldArmy) System.out.println("Wednesday won!");
        else System.out.println("Armies are equal!");
    }
}