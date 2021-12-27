package utils;

import actor.Actor;
import database.Database;
import entertainment.Film;
import entertainment.Genre;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.SerialInputData;
import fileio.MovieInputData;
import fileio.UserInputData;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Transfer {

    private Transfer() {
    }

    /*
        O metoda care cauta un utilizator dupa username-ul acestuia pe care il
    extragem din proprietatile unei actiuni ce trebuie realizata
     */

    /***
     *
     * @param action un obiect de tip ActionInputData
     * @return user-ul al carui username coincide cu cel din campul lui action, sau null in cazul
     * in care nu se gaseste un user cu username-ul cautat
     */
    public static User searchUserbyUsername(final ActionInputData action) {
        for (User user : Database.getDatabase().findAllUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                return user;
            }
        }
        return null;
    }

    /*
        Transforma o lista cu string-uri de reprezinta numele unor genuri
    intr-o lista cu elemente de tip Genre
     */

    /***
     *
     * @param listaGenuri o lista de string-uri ce reprezinta cate un gen
     * @return o lista de obiecte de tip Genre asociate string-urilor primite in lista data ca
     * parametru
     */
    public static List<Genre> stringListToGenreList(final List<String> listaGenuri) {
        List<Genre> genres = new ArrayList<>();
        for (String gen : listaGenuri) {
            genres.add(Utils.stringToGenre(gen));
        }
        return genres;
    }

    /*
        Parcurgem lista de actori din input si o copiem si in baza noastra de date
     */

    /***
     *
     * @param input un obiect de tip Input din care extragem actorii din lista specifica
     *              si pe care ii adaugam cu ajutorul constructorului nostru in baza
     *              de date
     */
    public static void addActorsInDatabase(final Input input) {
        List<Actor> actors = new ArrayList<>();
        for (ActorInputData actor : input.getActors()) {
            actors.add(new Actor(actor.getName(), actor.getCareerDescription(),
                    actor.getFilmography(), actor.getAwards()));
        }
        Database.getDatabase().addActors(actors);
    }

    /*
        Parcurgem lista de seriale din input si o copiem in baza noastra de date
     */

    /***
     *
     * @param input un obiect de tip Input din care extragem fiecare serial din lista sa de
     *              seriale si il adaugam si in baza noastra de date, in lista specifica
     *              cu ajutorul unui constructor.
     */
    public static void addSerialsInDatabase(final Input input) {
        List<Serial> serials = new ArrayList<>();
        for (SerialInputData serial : input.getSerials()) {
            List<Genre> genuri = stringListToGenreList(serial.getGenres());
            serials.add(new Serial(serial.getTitle(), serial.getYear(),
                    genuri, serial.getCast(), serial.getNumberSeason(), serial.getSeasons()));
        }
        Database.getDatabase().addShows(serials);
    }

   /*
        Parcurgem lista de filme din input si o copiem in baza noastra de date
    */

    /***
     *
     * @param input asemanator ca la seriale doar ca de aceasta data facem niste
     *              copii pentru filme pe care le adaugam in baza de date
     */
    public static void addMoviesInDatabase(final Input input) {
        List<Film> movies = new ArrayList<>();
        for (MovieInputData movie : input.getMovies()) {
            List<Genre> genuri = stringListToGenreList(movie.getGenres());
            movies.add(new Film(movie.getTitle(), movie.getYear(),
                    genuri, movie.getDuration(), movie.getCast()));
        }
        Database.getDatabase().addMovies(movies);
    }

    /*
        Parcurgem lista de useri din input si o copiem in baza noastra de date
        De aceasta data facem niste obiecte noi de tip history si favorites deoarece
        prin operatiile pe care le vom face asupra informatiilor din baza de date
        urmeaza sa le modificam.
     */

    /***
     *
     * @param input un obiect de tip Input din care extragem lista de useri pe care ii
     *              vom adauga in baza de date
     */
    public static void addUsersInDatabase(final Input input) {
        List<User> users = new ArrayList<>();
        for (UserInputData user : input.getUsers()) {
            String username = user.getUsername();
            String subscripiton = user.getSubscriptionType();
            Map<String, Integer> history = new HashMap<>(user.getHistory());
            ArrayList<String> favorites = new ArrayList<>(user.getFavoriteMovies());
            users.add(new User(username, subscripiton, history, favorites));
        }
        Database.getDatabase().addUsers(users);
    }

    /*
        Copiem toata lista de comenzi din input in baza noastra de date
     */

    /***
     *
     * @param input un obiect de tip Input din care selectam doar lista de comenzi
     *              pe care o vom introduce in totalitate in baza noastra de date
     */
    public static void addActionsInDatabase(final Input input) {
        Database.getDatabase().addActions(input.getCommands());
    }


    /***
     *
     * @param name numele dupa care vom cauta un serial
     * @return intoarce un obiect de tip Serial daca se gaseste un serial cu numele cautat
     * sau null in cazul in care nu se gaseste un obiect cu cerinta necesara
     */
    public static Serial searchSerialByTitle(final String name) {
        for (Serial serial : Database.getDatabase().findAllShows()) {
            if (serial.getName().equals(name)) {
                return serial;
            }
        }
        return null;
    }

    /***
     *
     * @param name numele dupa care vom cauta filmul
     * @return un obiect de tip Film daca gasim un film in baza de date cu proprietatea necesara
     * sau null in caz ca nu gasim niciun film cu numele cautat
     */
    public static Film searchMovieByTitle(final String name) {
        for (Film movie : Database.getDatabase().findAllMovies()) {
            if (movie.getName().equals(name)) {
                return movie;
            }
        }
        return null;
    }

    /***
     *
     * @param name numele dupa care sa cautam video-ul in baza de date
     * @return intoarce un obiect de tip Serial sau un obiect de tip Film
     * in functie de ce obiect din aceste tipuri gaseste in baza de date cu numele cerut
     */
    public static Video searchVideoByTitle(final String name) {
        Serial isSerial = searchSerialByTitle(name);
        Film isFilm = searchMovieByTitle(name);
        if (isSerial != null) {
            return isSerial;
        } else {
            return isFilm;
        }
    }

    /***
     *
     * @param name numele video-ului
     * @return 1 daca numele video-ului cautat coincide cu numele unui serial din baza de date
     * 2 daca numele video-ului cautat coincide cu numele unui film din baza de date
     * 0 daca numele video-ului cautat nu coincide nici cu numele unui film si nici
     * cu numele unui serial din baza de date.
     */
    public static int isAMovieOrAShow(final String name) {
        for (Serial serial : Database.getDatabase().findAllShows()) {
            if (serial.getName().equals(name)) {
                return 1;
            }
        }
        for (Film film : Database.getDatabase().findAllMovies()) {
            if (film.getName().equals(name)) {
                return 2;
            }
        }
        return 0;
    }

    /***
     * Metoda care actualizeaza campurile nrOfViews si appearancesInFavorites in functie de ce
     *     se afla in istoricul fiecarui utiilizator si in lista sa de favorite.
     */
    public static void actualiseViewsAndFavorites() {
        List<Video> allVideos = new ArrayList<>();
        allVideos.addAll(Database.getDatabase().findAllMovies());
        allVideos.addAll(Database.getDatabase().findAllShows());
        for (Video video : allVideos) {
            for (User user : Database.getDatabase().findAllUsers()) {
                if (user.getFavourite().contains(video.getName())) {
                    video.setAppearancesInFavorites(video.getAppearancesInFavorites() + 1);
                }
                if (user.getHistory().containsKey(video.getName())) {
                    video.setNrOfViews(video.getNrOfViews()
                           + user.getHistory().get(video.getName()));
                }
            }
        }
    }

}
