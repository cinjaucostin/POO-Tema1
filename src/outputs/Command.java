package outputs;

import common.Constants;
import database.Database;
import entertainment.Film;
import entertainment.Serial;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONObject;
import user.User;
import utils.Transfer;

import java.io.IOException;

public class Command {
    private JSONObject result;

    /***
     *
     * @return intoarce in JSONObject ce contine rezultatul unei comenzi.
     */
    public JSONObject getResult() {
        return result;
    }

    /***
     *
     * @param result seteaza rezultatul comenzii cu cel dat ca parametru.
     */
    public void setResult(final JSONObject result) {
        this.result = result;
    }

    /***
     *
     * @param action obiect de tip ActionInputData ce reprezinta actiunea pe care trebuie sa
     *               o efectuam.
     * @param fileWriter un obiect cu ajutorul caruia cream un JSONObject cu id-ul actiunii
     *                   si rezultatul acesteia.
     * @throws IOException exceptie de baza in caz de erori.
     */
    public void execCommandTasks(final ActionInputData action,
                                 final Writer fileWriter) throws IOException {
        if (action.getType().equals(Constants.VIEW)) {
            result = fileWriter.writeFile(action.getActionId(), "",
                    commandView(action));
        } else if (action.getType().equals(Constants.FAVORITE)) {
            result = fileWriter.writeFile(action.getActionId(), "",
                    commandFavorite(action));
        } else if (action.getType().equals(Constants.RATING)) {
            result = fileWriter.writeFile(action.getActionId(), "",
                    commandRating(action));
        }
    }

    /*
        Functii auxiliare pentru metodele de timp command
     */

    /***
     *
     * @param action un obiect de tip ActionInputData
     * @return filmul al carui nume coincide cu cel din action sau null
     * daca acest film nu este gasit in lista bazei de date.
     */
    public Film searchMovieByTitle(final ActionInputData action) {
        /*
        O metoda care cauta daca titlul dintr-o actiune este un film(daca da intoarce filmul,
    iar, daca nu este, intoarce null).
         */
        for (Film movie : Database.getDatabase().findAllMovies()) {
            if (movie.getName().equals(action.getTitle())) {
                return movie;
            }
        }
        return null;
    }

    /***
     *
     * @param action obiect de tip ActionInputData din care ne vom folosi
     *               de campul title.
     * @return serialul al carui nume coincide cu campul title din action.
     */
    public Serial searchSerialByTitle(final ActionInputData action) {
         /*
        O metoda care cauta daca titlul dintr-o actiune este un serial
    (daca da intoarce filmul, iar, daca nu este, intoarce null).
        */
        for (Serial show : Database.getDatabase().findAllShows()) {
            if (show.getName().equals(action.getTitle())) {
                return show;
            }
        }
        return null;
    }

    /***
     *
     * @param action obiectul de tip ActionInputData din care vom folosi campul title
     * @return filmul sau serialul al carui nume coincide cu title din action.
     */
    public Video searchVideoByTitle(final ActionInputData action) {
        /*
        O metoda care cauta daca titlul dintr-o actiune este un video din baza de date
     */
        if (searchMovieByTitle(action) != null) {
            return searchMovieByTitle(action);
        } else if (searchSerialByTitle(action) != null) {
            return searchSerialByTitle(action);
        }
        return null;
    }

    /***
     *
     * @param utilizator user-ul pentru care dorim sa verificam daca a mai vazut
     *                   un anumit video.
     * @param videoTitle numele video-ului pentru care vrem sa verificam conditia
     *                   anterioara.
     * @return 1 daca utilizatorul a mai vazut video-ul cu numele dat.
     *         0 daca utilizatorul nu a mai vazut video-ul.
     */
    public int wasSeen(final User utilizator, final String videoTitle) {
        /*
        Metoda va fi folosita in commandFavorite pentru a verifica daca un utilizator a vazut
     un video si daca, in cele din urma, poate adauga la favorite video-ul respectiv.
        */
        if (utilizator.getHistory().containsKey(videoTitle)) {
            return 1;
        } else {
            return 0;
        }
    }

    /***
     *
     * @param utilizator user-ul pentru care vrem sa verificam daca are un video in lista
     *                  de favorite.
     * @param videoTitle numele video-ului despre care vrem sa stim daca se afla in lista
     *                   de favorite a utilizatorului.
     * @return 1 daca video-ul se afla in lista de favorite a utilizatorului.
     *         0 daca video-ul nu se afla in aceasta lista.
     */
    public int existsAlreadyInFavorites(final User utilizator, final String videoTitle) {
        /*
        Metoda va fi folosita in commandFavorite pentru a verifica daca un utilizator are deja
     in lista de favorite un anumit video.
        */
        if (utilizator.getFavourite().contains(videoTitle)) {
            return 1;
        } else {
            return 0;
        }
    }

    /***
     *
     * @param utilizator user-ul pentru care vrem sa verificam daca a mai notat un anumit video
     * @param videoTitle titlul video-ului despre care vrem sa aflam daca a mai fost notat de
     *                   user-ul nostru.
     * @return 1 daca video-ul a mai fost notat.
     *          0 daca video-ul nu a mai fost notat.
     */
    public int wasAlreadyRated(final User utilizator, final String videoTitle) {
        /*
        Metoda care ne va spune daca un video a fost deja notat de catre un utilizator
        */
        if (utilizator.getRatings().containsKey(videoTitle)) {
            return 1;
        } else {
            return 0;
        }
    }

    /*
        COMMANDS
     */

    /***
     *
     * @param action obiectul de tip ActionInputData de care ne folosim pentru a realiza
     *               actiunea descrisa in el.
     * @return rezultatul actiunii efectuate dupa cum s-a precizat in cerinta temei.
     */
    public String commandFavorite(final ActionInputData action) {
        User utilizator = Transfer.searchUserbyUsername(action);
        String videoTitle = action.getTitle();
        if (utilizator != null) {
            if (wasSeen(utilizator, videoTitle) == 0) {
                return "error -> "
                        + action.getTitle() + " is not seen";
            } else if (existsAlreadyInFavorites(utilizator, videoTitle) == 1) {
                return "error -> "
                        + action.getTitle() + " is already in favourite list";
            } else {
                utilizator.getFavourite().add(videoTitle);
                Video videoclip = searchVideoByTitle(action);
                /*
                    Modificam numarul de aparitii in listele de favorite
                 ale video-ului respectiv
                 */
                videoclip.setAppearancesInFavorites(videoclip.getAppearancesInFavorites() + 1);
                return "success -> "
                        + action.getTitle() + " was added as favourite";
            }
        }
        return null;
    }

    /***
     *
     * @param action obiectul ActionInputData ce descrie actiunea pe care trebuie sa
     *               o realizam.
     * @return rezultatul actiunii dupa cum s-a precizat in cerinta.
     */
    public String commandView(final ActionInputData action) {
        /*
        Comanda care adauga un video in istoricul de vizioonare al unui utilizator
        */
        User utilizator = Transfer.searchUserbyUsername(action);
        String video = action.getTitle();
        if (utilizator != null) {
            /*
                Daca exista un video cu titlul din action in baza de date si un
            utilizator cu username-ul cautat.
                Actualizam numarul de vizualizari al videoclipului.
             */
            if (utilizator.getHistory().containsKey(video)) {
                int nrViews = utilizator.getHistory().get(video);
                utilizator.getHistory().replace(video, nrViews, nrViews + 1);
            } else {
                /*
                    //Daca videoclipul nu era in istoric atunci il adaugam cu
                 numarul de vizualizari 1.
                 */
                utilizator.getHistory().put(video, 1);
            }
            Video videoclip = searchVideoByTitle(action);
            if (videoclip != null) {
                /*
                    Actualizam numarul de vizualizari al respectivului videoclip folosindu-ne
                 de setterul campului nrOfViews.
                 */
                videoclip.setNrOfViews(videoclip.getNrOfViews() + 1);
            }
            return "success -> "
                    + action.getTitle() + " was viewed with total views of "
                    + utilizator.getHistory().get(video);


        } else {
            return "success -> "
                    + action.getTitle() + " can't be viewed";
        }
    }

    /***
     *
     * @param action obiect ActionInputData ce descrie actiunea pe care trebuie
     *               sa o efectuam.
     * @return rezultatul actiunii efectuate asa cum spune cerinta.
     */
    public String commandRating(final ActionInputData action) {
        User utilizator = Transfer.searchUserbyUsername(action);
        /*
            Daca exista un film cu titlul din action in baza de date este diferit de null
         */
        Film isAMovie = searchMovieByTitle(action);
        /*
            Daca exista un serial cu titlul din action in baza de date este diferit de null
         */
        Serial isAShow = searchSerialByTitle(action);
        if (utilizator != null) {
            if (isAMovie != null) {
                if (wasAlreadyRated(utilizator, isAMovie.getName()) == 1) {
                    return "error -> "
                            + action.getTitle() + " has been already rated";
                } else {
                    if (wasSeen(utilizator, isAMovie.getName()) == 1) {
                        commandRatingForMovie(isAMovie, utilizator, action.getGrade());
                        return "success -> "
                                + isAMovie.getName() + " was rated with " + action.getGrade()
                                + " by " + utilizator.getUsername();
                    } else if (wasSeen(utilizator, isAMovie.getName()) == 0) {
                        return "error -> "
                                + action.getTitle() + " is not seen";
                    }
                }
            } else if (isAShow != null) {
                String name = isAShow.getName() + action.getSeasonNumber();
                if (wasAlreadyRated(utilizator, name) == 1) {
                    return "error -> "
                            + action.getTitle() + " has been already rated";
                } else {
                    if (wasSeen(utilizator, isAShow.getName()) == 1) {
                        commandRatingForShow(isAShow, utilizator,
                                action.getGrade(), action.getSeasonNumber());
                        return "success -> "
                                + isAShow.getName() + " was rated with " + action.getGrade()
                                + " by " + utilizator.getUsername();
                    } else {
                        return "error -> "
                                + action.getTitle() + " is not seen";
                    }
                }
            }
        }
        return null;
    }

    /***
     *
     * @param show serialul al carui sezon trebuie notat.
     * @param user user-ul care ofera rating-ul.
     * @param grade nota user-ului.
     * @param nrSeason numarul sezonului pe care trebuie sa il notam
     */
    public void commandRatingForShow(final Serial show, final User user,
                                     final double grade, final int nrSeason) {
        /*
            Adaugam ratingul la respectivul serial in contul userului
         */
        String name = show.getName() + nrSeason;
        if (Database.getDatabase().findAllUsers().contains(user)) {
            user.getRatings().put(name, grade);
        }
        /*
            Adaugam ratingul si pentru show in baza de date.
        */
        show.getSeasons().get(nrSeason - 1).getRatings().add(grade);
    }

    /***
     *
     * @param movie filmul care trebuie notat.
     * @param user user-ul care ofera nota filmului.
     * @param grade nota respectiva.
     */
    public void commandRatingForMovie(final Film movie, final User user,
                                      final double grade) {
        /*
            Adaugam ratingul in lista de rating-uri a utilizatorului.
         */
        if (Database.getDatabase().findAllUsers().contains(user)) {
            user.getRatings().put(movie.getName(), grade);
        }
        /*
            Adaugam ratingul in lista de rating-uri a utilizatorului.
         */
        movie.getRatings().add(grade);
    }

}
