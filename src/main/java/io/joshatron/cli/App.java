package io.joshatron.cli;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class App
{
    public static void main(String[] args) {
        try {
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(TerminalBuilder.terminal())
                    .completer(new StringsCompleter("trainer", "player"))
                    .build();
            System.out.println("---------------------");
            System.out.println("| Welcome to TakCLI |");
            System.out.println("---------------------");
            System.out.println();
            while (true) {
                String input = lineReader.readLine("Are you trainer or player? ").trim();
                if (input.toLowerCase().equals("trainer")) {
                    Trainer.setupTraining();
                    break;
                } else if (input.toLowerCase().equals("player")) {
                    GamePlayer.runGameSet();
                    break;
                }
                else {
                    System.out.println("Please enter either training or playing");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
