package com.example.demo.stl_figure;

public class Constant {
    public static String line1 = "Invalid solid format. line 1";
    public static String line = "Invalid facet format. line ";
    public static String binaryReadImplementation = "Binary STL file parsing is not implemented yet.";
    public static String notDouble = "Some value in you file are not double value";
    public static String vertex = "vertex ";
    public static String solid = "solid ";

    public static String normal = "facet normal ";
    public static String space = " ";
    public static String empty = "";
    public static String invalidColor = "Invalid color code, exemple .123;.23;.34 or .123;.34;.34;0.2";
    public static String comma = ";";
    public static String invalidColorComponent = "Invalid color component. one of your color component are not double";
    public static String enterIp = "Enter IP:";
    public static String selectOption = "Select Option:";
    public static String validateButton = "Validate";
    public static String exitConfirmation = "Exit Confirmation";
    public static String wantToExit = "Are you sure you want to exit?";
    public static String choose = "Click OK to exit or Cancel to stay.";
    public static String dialogTitle = "Connect Hub";
    public static String appTitle = "STL visualizer";
    public static String menu1 = "File";
    public static String menuItem1 = "Connect";
    public static String menuItem2 = "Exit";

    public static String lineXtoX(int i) {
        return "Invalid vertex format. Look between line " + (i + 1) + " and " + (i + 3);
    }
}
