package com.example.demo.Utils;

public class Strings {
    public static String empty = "";
    public static String space = " ";
    public static final String SRC_MAIN_RESOURCES = "src/main/resources/com/example/demo/data/";
    public static final String UNKNOWN_FILE = "The file you provided does not match any supported format";
    public static final String NOT_A_NUMBER = "Some value in your stl file are not a number, he's suppose to be";
    public static final String WRONG_VERTEX_FORMAT = "Bad format on vertex lines, check and try again";
    public static final String WAITING = "Waiting for connexion";
    public static final String SOMEONE_CONNECTED = "Someone connected";
    public static final String CONNECTED = "Connected";
    public static final String TRYING = "Trying connect to server";
    public static final String UNKNOWN_HOST = "Host unknown: ";
    public static final String CONNEXION_FAIL = "Connexion failed";

    public static final String START_OF_VERTEX = "vertex ";
    public static final String START_OF_ASCII = "solid ";
    public static final String START_OF_BIN = "Binary STL";
    public static final String START_OF_FACET = "facet normal ";
    public static final String START_OF_TRIANGLE = "Triangle ";
    public static final String AXE_X = "X";
    public static final String AXE_Y = "Y";
    public static final String AXE_Z = "Z";
    public static final String END_OF_COLOR = ";1";
    public static final String COLOR_BLUE = "0;0;1;1";
    public static final String COLOR_GREEN = "0;1;0;1";
    public static final String COLOR_RED = "1;0;0;1";
    public static final String ROTATION_POINT = "150;50;100";
    public static final int DEFAULT_PORT = 4500;

    public static final String ROTATION = "ROTATION ";
    public static final String ROTATE = "ROTATE ";
    public static final String TRANSLATE = "TRANSLATE ";
    public static final String POSITION = "POSITION ";
    public static final String COLOR = "COLOR ";
    public static final String POINT = "POINT ";
    public static final String DATA = "DATA ";
    public static final String AXE = "AXIS ";


    public static final String DEFAULT_NAME_FOR_BIN_STL = "Bin file";

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


    public static String errorLine(int i) {
        return "You have some error at line " + i + ". Please check it and try again.";
    }
}
