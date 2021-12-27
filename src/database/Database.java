package database;

import actor.Actor;
import entertainment.Film;
import entertainment.Serial;
import fileio.ActionInputData;
import user.User;

import java.util.ArrayList;
import java.util.List;


public final class Database {
    private static Database instance = null;
    private static final List<Actor> DATABASE_ACTORS = new ArrayList<>();
    private static final List<Film> DATABASE_MOVIES = new ArrayList<>();
    private static final List<Serial> DATABASE_SHOWS = new ArrayList<>();
    private static final List<User> DATABASE_USERS = new ArrayList<>();
    private static final List<ActionInputData> DATABASE_ACTIONS = new ArrayList<>();

    private Database() {
    }

    /***
     *
     * @return o instanta noua a lui Database daca nu exista deja una, sau instanta
     * deja existenta.
     */
    public static Database getDatabase() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /***
     *
     * @param actors primeste o lista de actori pe care o va adauga cu totul
     *               in baza de date
     */
    public void addActors(final List<Actor> actors) {
        Database.DATABASE_ACTORS.addAll(actors);
    }

    /***
     *
     * @param movies primeste o lista de filme pe care o adauga in totalitate
     *               in lista de filme din baza de date.
     */
    public void addMovies(final List<Film> movies) {
        Database.DATABASE_MOVIES.addAll(movies);
    }

    /***
     *
     * @param shows primeste o lista de seriale pe care o adauga in lista de seriale
     *              a bazei de date.
     */
    public void addShows(final List<Serial> shows) {
        Database.DATABASE_SHOWS.addAll(shows);
    }

    /***
     *
     * @param users o lista de useri pe care o adauga in totalitate in lista
     *              de useri a bazei de date.
     */
    public void addUsers(final List<User> users) {
        Database.DATABASE_USERS.addAll(users);
    }

    /***
     *
     * @param actions o lista de actiuni pe care o adauga in totalitate in baza noastra
     *                de date.
     */
    public void addActions(final List<ActionInputData> actions) {
        Database.DATABASE_ACTIONS.addAll(actions);
    }

    /***
     *
     * @return o lista cu toti actorii din baza de date.
     */
    public List<Actor> findAllActors() {
        return Database.DATABASE_ACTORS;
    }

    /***
     *
     * @return o lista cu toate filmele din baza de date.
     */
    public List<Film> findAllMovies() {
        return Database.DATABASE_MOVIES;
    }

    /***
     *
     * @return o lista cu toate serialele din baza de date.
     */
    public List<Serial> findAllShows() {
        return Database.DATABASE_SHOWS;
    }

    /***
     *
     * @return o lista cu toti userii din baza de date.
     */
    public List<User> findAllUsers() {
        return Database.DATABASE_USERS;
    }

    /***
     *
     * @return o lista cu toate actiunile din baza de date
     */
    public List<ActionInputData> findAllActions() {
        return Database.DATABASE_ACTIONS;
    }

}
