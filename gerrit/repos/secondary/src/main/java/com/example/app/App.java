package com.example.app;

/**
 * Generates arguments for invoking the string generator in primary project
 */
public class App 
{
    public static void main( String[] args ) {
        // TWO MODES: normal and legacy
        StringBuilder output = new StringBuilder()
                .append(" -d 10")
//                .append("-n")  //Unsupported
                .append(" -l 50");
        System.out.println(output);
    }
}
