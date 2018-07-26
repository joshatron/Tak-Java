package io.joshatron.cli;

import io.joshatron.neuralnet.BackPropTrainer;
import io.joshatron.engine.*;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        try {
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(TerminalBuilder.terminal())
                    .completer(new StringsCompleter("training", "playing"))
                    .build();
            System.out.println("---------------------");
            System.out.println("| Welcome to TakCLI |");
            System.out.println("---------------------");
            System.out.println();
            while (true) {
                String input = lineReader.readLine("Are you training or playing? ").trim();
                if (input.toLowerCase().equals("training")) {
                    BackPropTrainer.runTrainingSet();
                    break;
                } else if (input.toLowerCase().equals("playing")) {
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
