package outputs;

import actor.Actor;
import common.Constants;
import database.Database;
import entertainment.Genre;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONObject;
import user.User;
import utils.Sortings;
import utils.Transfer;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Query {
    private JSONObject result;

    /***
     *
     * @return un obiect de tip JSONObject ce contine rezultatul Query-ului.
     */
    public JSONObject getResult() {
        return result;
    }

    /***
     *
     * @param result seteaza rezultatul query-ului.
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
    public void execQueryTask(final ActionInputData action,
                              final Writer fileWriter) throws IOException {
        if (action.getObjectType().equals(Constants.USERS)) {
            List<User> firstUsers = firstNUsers(action);
            result = fileWriter.writeFile(action.getActionId(), "",
                    "Query result: " + firstUsers.toString());
        } else if (action.getObjectType().equals(Constants.ACTORS)) {
            if (action.getCriteria().equals(Constants.AVERAGE)) {
                List<Actor> firstActorsByAverage = firstNActors(action);
                result = fileWriter.writeFile(action.getActionId(), "",
                        "Query result: " + firstActorsByAverage.toString());
            } else if (action.getCriteria().equals(Constants.AWARDS)) {
                List<Actor> actorsByAwards = actorAwardsQueries(action);
                result = fileWriter.writeFile(action.getActionId(), "",
                        "Query result: " + actorsByAwards.toString());
            } else if (action.getCriteria().equals(Constants.FILTER_DESCRIPTIONS)) {
                List<Actor> actorsByWords = actorWordsQueries(action);
                result = fileWriter.writeFile(action.getActionId(), "",
                        "Query result: " + actorsByWords.toString());
            }

        } else if (action.getObjectType().equals(Constants.MOVIES)
                || action.getObjectType().equals(Constants.SHOWS)) {
            if (action.getCriteria().equals(Constants.MOST_VIEWED)) {
                List<Video> videos = videosByYearGenreViews(action);
                result = fileWriter.writeFile(action.getActionId(), "",
                        "Query result: " + videos.toString());
            } else if (action.getCriteria().equals(Constants.RATINGS)) {
                List<Video> videos = videosByYearGenreRating(action);
                result = fileWriter.writeFile(action.getActionId(), "",
                        "Query result: " + videos.toString());
            } else if (action.getCriteria().equals(Constants.FAVORITE)) {
                List<Video> videos = videosByYearGenreFavorites(action);
                result = fileWriter.writeFile(action.getActionId(), "",
                        "Query result: " + videos.toString());
            } else if (action.getCriteria().equals(Constants.LONGEST)) {
                List<Video> videos = videosByYearGenreDuration(action);
                result = fileWriter.writeFile(action.getActionId(), "",
                        "Query result: " + videos.toString());
            }
        }
    }
    /*
        ACTORS QUERIES
     */

    /***
     *
     * @param action obiectul ce descrie actiunea pe care trebuie sa o facem.
     * @return lista cu primii n actori sortati dupa medie.
     */
    public List<Actor> firstNActors(final ActionInputData action) {
        /*
            Lista cu primii N actori sortati dupa medie
        */
        List<Actor> allActors = new ArrayList<>(Database.getDatabase().findAllActors());
        List<Actor> nActors = new ArrayList<>();
        /*
            Sortam lista de actori dupa media acestora pe care o calculam
        cu ajutorul metodei getAverage definita in clasa Actor
         */
        List<Actor> allActorsSorted = Sortings.sortActorsByAverageAndName(allActors,
                action.getSortType());
        /*
            Extragem primii N actori din lista de actori a bazei de date
         */
        if (action.getNumber() > allActorsSorted.size()) {
            nActors.addAll(allActorsSorted);
        } else {
            int k = 1;
            for (Actor actor : allActorsSorted) {
                if (k > action.getNumber()) {
                    break;
                } else if (actor.getAverage() != 0) {
                    nActors.add(actor);
                    k++;
                }
            }
        }
        return nActors;
    }

    /***
     *
     * @param action obiectul ce descrie actiunea noastra.
     * @return o lista cu toti actorii care au castigat anumite award-uri
     * sortati intai dupa numarul total de premii si apoi dupa nume, daca
     * este cazul.
     */
    public List<Actor> actorAwardsQueries(final ActionInputData action) {
        /*
            Lista cu actori dupa premiile castigate
        */
        List<Actor> actorsWithAwards = new ArrayList<>();
        for (Actor actor : Database.getDatabase().findAllActors()) {
            int flag = 1;
            for (String award : action.getFilters().get(Constants.AWARDS_FILTER_POSITION)) {
                if (!actor.getAwards().containsKey(Utils.stringToAwards(award))) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                actorsWithAwards.add(actor);
            }
        }
        return Sortings.sortActorsByTotalAwardsAndName(
                actorsWithAwards, action.getSortType());
    }

    /***
     *
     * @param action obiectul ce descrie actiunea ce trebuie efectuata.
     * @return o lista cu toti actorii care contin cuvintele date in filtre
     * in descrierea carierei lor sortati dupa nume.
     */
    public List<Actor> actorWordsQueries(final ActionInputData action) {
        /*
            Lista cu actorii care au in descriere anumite cuvinte
        */
        List<Actor> actorsWithWords = new ArrayList<>();
        for (Actor actor : Database.getDatabase().findAllActors()) {
            String description = actor.getCareerDescription().replaceAll("[^a-zA-Z ]", " ");
            int flag = 1;
            for (String word : action.getFilters().get(Constants.WORDS_FILTER_POSITION)) {
                word = word.toLowerCase();
                if (!description.toLowerCase().contains(" " + word + " ")) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                actorsWithWords.add(actor);
            }
        }
        return Sortings.sortActorsByName(actorsWithWords, action.getSortType());
    }

    /*
        VIDEO QUERIES
     */

    /***
     *
     * @param allVideos o lista cu video-uri
     * @param action obiectul ce descrie actiunea ce trebuie sa o facem.
     * @return o lista cu toate video-urile care contin genurile prezente
     * in filtrele din action.
     */
    public List<Video> videosByGenre(final List<Video> allVideos,
                                     final ActionInputData action) {
        /*
            Metoda care ne intoarce videoclipurile ce au un anumit gen
        */
        List<Video> videosWithGenre = new ArrayList<>();
        List<Genre> genuri = new ArrayList<>();
        if (action.getFilters().get(Constants.GENRE_FILTER_POSITION) != null) {
            genuri = Transfer.stringListToGenreList(action.getFilters().get(1));
        }
        for (Video video : allVideos) {
            int flag = 1;
            for (Genre gen : genuri) {
                if (!video.getGenres().contains(gen)) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                /*
                    Inseamna ca am gasit un film care respecta filtrele noastre
                si il adaugam in lista.
                 */
                videosWithGenre.add(video);
            }
        }
        return videosWithGenre;
    }

    /***
     *
     * @param allVideos o lista de video-uri.
     * @param action o actiune.
     * @return o lista de video-uri care au aparut in anul specificat
     * in filtrele actiunii.
     */
    public List<Video> videosByYear(final List<Video> allVideos,
                                    final ActionInputData action) {
        /*
            Metoda care ne intoarce videoclipurile ce au aparut intr-un anumit an
        */
        List<Video> videosWithYear = new ArrayList<>();
        int year;
        if (action.getFilters().get(Constants.YEAR_FILTER_POSITION) != null) {
            year = Integer.parseInt(action.getFilters().get(Constants.YEAR_FILTER_POSITION).
                    get(Constants.FIRST_FILTER));
        } else {
            return allVideos;
        }
        for (Video video : allVideos) {
            if (video.getYear() == year) {
                videosWithYear.add(video);
            }
        }
        return videosWithYear;
    }

    /***
     *
     * @param allVideos lista de video-uri.
     * @param action actiunea ce trebuie efectuata.
     * @return o lista cu video-urile care au aparut in anul prezent in filtrele din action
     * si care contine genurile prezente in filtre.
     */
    public List<Video> videosByYearAndGenre(final List<Video> allVideos,
                                            final ActionInputData action) {
        /*
            Metoda care ne intoarce videoclipurile din baza de date care au
        aparut intr-un anumit an si au un anumit gen
        */
        List<Video> videosWithYearAndGenre = new ArrayList<>();
        List<Genre> genuri = new ArrayList<>();
        int year = 0;
        if (action.getFilters().get(Constants.YEAR_FILTER_POSITION) != null
                && action.getFilters().get(Constants.GENRE_FILTER_POSITION) != null) {
            genuri = Transfer.stringListToGenreList(action.getFilters().get(1));
            year = Integer.parseInt(action.getFilters().get(0).get(0));
        }

        for (Video video : allVideos) {
            if (video.getYear() == year) {
                int flag = 1;
                for (Genre gen : genuri) {
                    if (!video.getGenres().contains(gen)) {
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                /*
                    Inseamna ca am gasit un film care respecta filtrele noastre
                si il adaugam in lista.
                 */
                    videosWithYearAndGenre.add(video);
                }
            }
        }
        return videosWithYearAndGenre;
    }

    /***
     *
     * @param action obiectul ce descrie actiunea ce trebuie realizata.
     * @return intoarce 0 daca ambele campuri specifice anului si genurilor
     * din action sunt nule
     *         intoarce 1 daca campul cu anul din filtre este diferit de null
     * si campul cu genurile este null
     *         intoarce 2 daca campul cu anul este null si campul cu genurile
     * diferit de null
     *         intoarce 3 daca ambele campuri sunt diferite de null
     */
    public int verifyWhichFiltersExists(final ActionInputData action) {
        if (action.getFilters().get(Constants.YEAR_FILTER_POSITION).
                get(Constants.FIRST_FILTER) == null
                && action.getFilters().get(Constants.GENRE_FILTER_POSITION).
                get(Constants.FIRST_FILTER) == null) {
            return 0;
        } else if (action.getFilters().get(Constants.YEAR_FILTER_POSITION).
                get(Constants.FIRST_FILTER) != null
                && action.getFilters().get(Constants.GENRE_FILTER_POSITION).
                get(Constants.FIRST_FILTER) == null) {
            return 1;
        } else if (action.getFilters().get(Constants.YEAR_FILTER_POSITION).
                get(Constants.FIRST_FILTER) == null
                && action.getFilters().get(Constants.GENRE_FILTER_POSITION).
                get(Constants.FIRST_FILTER) != null) {
            return 2;
        }
        /*
         * Aici primeam ceva eroare de checkstyle cum ca ar fi magic number 3
         * asa ca am facut o constanta cu valoarea 3.
         */
        return Constants.THREE;
    }

    /***
     *
     * @param action actiunea ce trebuie efectuata
     * @return o lista cu video-urile extrase in functie de criteriile din action
     */
    public List<Video> videosByYearOrGenre(final ActionInputData action) {
        List<Video> allVideos = new ArrayList<>();
        List<Video> videosWithYearOrGenre = new ArrayList<>();
        allVideos.addAll(Database.getDatabase().findAllMovies());
        allVideos.addAll(Database.getDatabase().findAllShows());
        /*
            Deoarece filtrele ne sunt date in action ca niste string-uri va trebui sa parsam la
         intvaloarea anului din filtrul pentru an.
            De asemenea, vom verifica daca campurile cu filtrele nu sunt null.
            Daca functia verifyWhichFiltersExists intoarce 0 inseamna ca in lista vor ramane toate
         video-urile, daca intoarce 1 inseamna ca va trebui sa extragem video-urile doar dupa anul
         specificat in filtre, daca intoarce 2 va trebui sa extragem doar dupa genurile din filtre
         si daca intoarce 3 va trebui sa extragem atat dupa anul cat si dupa genurile din filtre.
         */
        if (verifyWhichFiltersExists(action) == 0) {
            videosWithYearOrGenre = allVideos;
        } else if (verifyWhichFiltersExists(action) == 1) {
            videosWithYearOrGenre = videosByYear(allVideos, action);
        } else if (verifyWhichFiltersExists(action) == 2) {
            videosWithYearOrGenre = videosByGenre(allVideos, action);
        } else if (verifyWhichFiltersExists(action) == Constants.THREE) {
            videosWithYearOrGenre = videosByYearAndGenre(allVideos, action);
        }
        return videosWithYearOrGenre;
    }

    /***
     *
     * @param action actiunea ce trebuie sa o efectuam.
     * @param videos lista de videoclipuri din care trebuie sa extragem doar filmele
     *               sau doar serialele in functie de care este ObjectType-ul
     *               actiunii.
     */
    public void extractShowsOrMovies(final ActionInputData action,
                                     final List<Video> videos) {
        /*
            Metoda cu care extragem dintr-o lista de video-uri doar filmele, sau doar
        serialele, dupa cum ne este specificat in campul objectType al unei actiuni.
        */
        if (action.getObjectType().equals("movies")) {
            /*
                    Verificam daca video-ul din lista este serial.
                    In acest  caz functia definita special in Transfer ar trebui sa returneze 1.
                    Daca aceasta returneaza 1 noi vom elimina serialul din lista deoarece noi
                 avem nevoie doar de filmle in lista noastra.
                 */
            videos.removeIf(video -> Transfer.isAMovieOrAShow(video.getName()) == 1);
        } else if (action.getObjectType().equals("shows")) {
            /*
                    In acest caz noua ne trebuie doar serialele ce indeplinesc conditiile.
                    De aceea ne vom folosi de functia isAMovieOrAShow pentru a vedea daca
                 video-ul curent este un film(functia ar trebui sa returneze 2), si in cazul
                 in care este, atunci vom elimina filmul din lista.
                 */
            videos.removeIf(video -> Transfer.isAMovieOrAShow(video.getName()) == 2);
        }
    }

    /***
     *
     * @param action actiunea ce trebuie efectuata
     * @param videos lista de video-uri din care trebuie sa le extragem
     *               doar pe primele N, unde N este specificat in
     *               action.
     */
    public void extractFirstNVideos(final ActionInputData action,
                                    final List<Video> videos) {
        if (action.getNumber() < videos.size() && videos.size() != 0) {
            while (videos.size() != action.getNumber()) {
                videos.remove(videos.size() - 1);
            }
        }
    }

    /***
     *
     * @param action actiunea ce trebuie efectuata.
     * @return lista de video-uri selectate dupa an, genuri si sortate dupa rating.
     */
    public List<Video> videosByYearGenreRating(final ActionInputData action) {
        /*
            Lista cu primele N video-uri dintr-un anumit an si care au un anumit gen,
        sortate dupa rating.
            Apelam metoda care ne intoarce primele N filme ce au aparut
        intr-un anumit an si au un anumit gen.
         */
        List<Video> videosWithYearAndGenreByRating = videosByYearOrGenre(action);
        extractShowsOrMovies(action, videosWithYearAndGenreByRating);
        List<Video> videosSorted = Sortings.sortVideoByRatingAndName(
                videosWithYearAndGenreByRating, action.getSortType());
        extractFirstNVideos(action, videosSorted);
        /*
            Eliminam fiecare video ce are rating-ul 0
         */
        videosSorted.removeIf(video -> video.getAverageRating() == 0);
        /*
            Avem lista ceruta, trebuie doar sa o sortam acum dupa rating
         */
        return videosSorted;
    }

    /***
     *
     * @param action actiunea
     * @return lista de video-uri selectate dupa an, genuri si sortate dupa numarul
     * de vizualizari.
     */
    public List<Video> videosByYearGenreViews(final ActionInputData action) {
        /*
            Lista cu primele N filme dupa: an, gen, sortate dupa numar de vizualizari
            Ne folosim de metoda definita anterior pentru a extrage filmele
         ce au aparut in anul si au genul din filtre.
         */
        List<Video> videosByYearGenreByViews = videosByYearOrGenre(action);
        /*
            Ne ramane doar sa sortam lista de filme, de aceasta data
         dupa numarul de vizualizari.
         */
        extractShowsOrMovies(action, videosByYearGenreByViews);
        List<Video> videosSorted = Sortings.sortVideoByViewsAndName(
                videosByYearGenreByViews, action.getSortType());
        extractFirstNVideos(action, videosSorted);
        videosSorted.removeIf(video -> video.getNrOfViews() == 0);
        return videosSorted;
    }

    /***
     *
     * @param action actiunea
     * @return lista de video-uri selectate dupa an si genuri si sortate
     * dupa durata.
     */
    public List<Video> videosByYearGenreDuration(final ActionInputData action) {
        /*
            Lista cu primele N filme dupa: an, gen, sortate dupa durata
        */
        List<Video> videosWithYearGenreByDuration = videosByYearOrGenre(action);
        extractShowsOrMovies(action, videosWithYearGenreByDuration);
        /*
            In acest caz sortam dupa durata filmului
         */
        List<Video> videosSorted = Sortings.sortVideoByDurationAndName(
                videosWithYearGenreByDuration, action.getSortType());
        extractFirstNVideos(action, videosSorted);
        videosSorted.removeIf(video -> video.getDurationSum() == 0);
        return videosSorted;
    }

    /***
     *
     * @param action actiunea
     * @return lista de video-uri selectate dupa an si gen si sortate
     * dupa numarul de aparitii in listele de favorite.
     */
    public List<Video> videosByYearGenreFavorites(final ActionInputData action) {
        /*
            Lista cu primele N filme dupa:an, gen si sortate dupa numarul de aparitii in favorite.
        */
        List<Video> videosWithYearGenreByFavorites = videosByYearOrGenre(action);
        /*
            In acest caz sortam dupa numarul de aparitii in favorite.
         */
        extractShowsOrMovies(action, videosWithYearGenreByFavorites);
        List<Video> videosSorted = Sortings.sortVideoByFavoritesAndName(
                videosWithYearGenreByFavorites, action.getSortType());
        extractFirstNVideos(action, videosSorted);
        videosSorted.removeIf(video -> video.getAppearancesInFavorites() == 0);
        return videosSorted;
    }

    /*
        USER QUERIES
     */

    /***
     *
     * @param action obiectul ce descrie actiunea ce trebuie efectuata.
     * @return o lista cu primii N utilizatori, unde N este specificat in action,
     * sortati dupa numarul de rating-uri oferite.
     */
    public List<User> firstNUsers(final ActionInputData action) {
        /*
            Lista cu primii N utilizatori sortati dupa numarul de ratings pe care l-au oferit.
        */
        List<User> nUsers = new ArrayList<>();
        List<User> allUsers = new ArrayList<>(Database.getDatabase().findAllUsers());
        List<User> usersSorted = Sortings.sortUserByRatingAndName(
                allUsers, action.getSortType());
        int k = 0;
        for (User user : usersSorted) {
            if (k > action.getNumber() - 1) {
                return nUsers;
            }
            if (user.getRatings().size() != 0) {
                nUsers.add(user);
                k++;
            }
        }
        return nUsers;
    }

}
