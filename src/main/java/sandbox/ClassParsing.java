package sandbox;

import game.play.Subclass;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClassParsing {
    public static void main(String[] args) {

    }

    public static Subclass parseHomebrewSubclass(String filepath) {
        try (Scanner scanner = new Scanner(new File(filepath))) {

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filepath);
        }
        return null;
    }
}
