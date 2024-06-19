package sandbox;

import game.items.CustomWeapon;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.util.Scanner;

public class WeaponTransfer {
    public enum Operation {
        PASTE, CONVERT, PRINT, EXIT
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Operation operation = null;
        try {
            operation = getOperation(scanner);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        while (operation != Operation.EXIT) {
            if (operation != null) {
                switch (operation) {
                    case PASTE -> pasteWeapon(scanner);
                    case CONVERT -> convertWeapon(scanner);
                    case PRINT -> printWeapon(scanner);
                }
            }
            try {
                operation = getOperation(scanner);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Operation getOperation(Scanner in) {
        System.out.println("Enter the operation code:");
        String sb = """
                V - Paste weapon
                C - Convert weapon
                P - Print weapon
                E - Exit""";
        System.out.println(sb);
        String code = in.nextLine().trim().toUpperCase();
        if (code.length() != 1) {
            throw new IllegalArgumentException("Invalid operation code: " + code);
        }
        return switch (code.charAt(0)) {
            case 'V' -> Operation.PASTE;
            case 'C' -> Operation.CONVERT;
            case 'P' -> Operation.PRINT;
            case 'E' -> Operation.EXIT;
            default -> throw new IllegalArgumentException("Invalid operation code: " + code);
        };
    }

    private static void pasteWeapon(Scanner in) {
        // Access System clipboard and paste the weapon
        System.out.println("Pasting weapon...");

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String info = "";
        try {
            info = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Saving weapon to file...");
        String name = info.split("\n")[0];
        try (PrintWriter out = new PrintWriter("src/test/" + name + ".txt")) {
            out.print(info);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void convertWeapon(Scanner in) {
        System.out.println("Converting weapon...");
        System.out.print("Enter the weapon name: ");
        String name = in.nextLine().trim().toLowerCase();

        String filename = CustomWeapon.Factory.convertTXT("src/test/" + name + ".txt");
        System.out.println("Weapon converted to " + filename);
    }

    private static void printWeapon(Scanner in) {
        System.out.println("Printing weapon...");
        System.out.print("Enter the weapon name: ");
        String name = in.nextLine().trim().toLowerCase()
                .replace(" ", "_").replace("'", "");
        try {
            CustomWeapon weapon = CustomWeapon.Builder.readFromFile("src/test/official/" + name + ".weapon");
            System.out.println(weapon);
        } catch (ProtocolException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
