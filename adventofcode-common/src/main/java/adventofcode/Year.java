package adventofcode;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

@Command(mixinStandardHelpOptions = true, description = "Run Advent of Code puzzles for a year")
public class Year implements Callable<Integer> {
    public static void main(String[] args) throws Exception {
        CommandLine cmd = new CommandLine(new Year());
        addDayCommands(cmd);
        System.exit(cmd.execute(args));
    }

    private static void addDayCommands(CommandLine cmd) throws Exception {
        Reflections reflections = new Reflections("adventofcode");
        List<Class<? extends AbstractDay>> dayClasses = new ArrayList<>(reflections.getSubTypesOf(AbstractDay.class));
        dayClasses.sort(Comparator.comparing(Class::getSimpleName));
        for (Class<?> dayClass : dayClasses) {
            AbstractDay day = dayClass.asSubclass(AbstractDay.class).getConstructor().newInstance();
            cmd.addSubcommand(dayClass.getSimpleName(), day);
        }
    }

    @Override
    public Integer call() {
        System.out.println("Specify a puzzle day to run. Use --help to see available days.");
        return 0;
    }
}