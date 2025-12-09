package adventofcode;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import org.reflections.Reflections;

import adventofcode.utils.decorators.Decorators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

@Command(mixinStandardHelpOptions = true, description = "Run Advent of Code puzzles for a year")
public class Year implements Callable<Integer> {
    public static void main(String[] args) throws Exception {
        CommandLine cmd = new CommandLine(new Year());
        for (AbstractDay day : getDays())
            cmd.addSubcommand(Decorators.undecorate(day.getClass()).getSimpleName(), day);
        System.exit(cmd.execute(args));
    }

    private static List<? extends AbstractDay> getDays() throws Exception {
        Reflections reflections = new Reflections("adventofcode");
        Set<Class<? extends AbstractDay>> dayClasses = reflections.getSubTypesOf(AbstractDay.class);

        List<AbstractDay> days = new ArrayList<>();
        for (Class<? extends AbstractDay> dayClass : dayClasses)
            days.add(Decorators.decorate(dayClass).getConstructor().newInstance());

        days.sort(Comparator.comparingInt(day -> day.day));

        return days;
    }

    @Override
    public Integer call() {
        System.out.println("Specify a puzzle day to run. Use --help to see available days.");
        return 0;
    }
}