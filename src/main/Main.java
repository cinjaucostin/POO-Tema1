package main;

import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import database.Database;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import outputs.Command;
import outputs.Query;
import outputs.Recommendation;
import utils.Transfer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    @SuppressWarnings("unchecked")
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation

        Database.getDatabase().findAllMovies().clear();
        Database.getDatabase().findAllShows().clear();
        Database.getDatabase().findAllUsers().clear();
        Database.getDatabase().findAllActions().clear();
        Database.getDatabase().findAllActors().clear();

        Transfer.addActionsInDatabase(input);
        Transfer.addUsersInDatabase(input);
        Transfer.addActorsInDatabase(input);
        Transfer.addMoviesInDatabase(input);
        Transfer.addSerialsInDatabase(input);
        Transfer.actualiseViewsAndFavorites();

        for (ActionInputData action : Database.getDatabase().findAllActions()) {
            if (action.getActionType().equals(Constants.COMMAND)) {
                Command result = new Command();
                result.execCommandTasks(action, fileWriter);
                arrayResult.add(result.getResult());
            } else if (action.getActionType().equals(Constants.QUERY)) {
                Query result = new Query();
                result.execQueryTask(action, fileWriter);
                arrayResult.add(result.getResult());
            } else if (action.getActionType().equals(Constants.RECOMMENDATION)) {
                Recommendation result = new Recommendation();
                result.execRecommendationTask(action, fileWriter);
                arrayResult.add(result.getResult());
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}
