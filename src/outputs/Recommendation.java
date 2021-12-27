package outputs;

import common.Constants;
import database.Database;
import entertainment.Film;
import entertainment.Genre;
import entertainment.Serial;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.HashMap;


public class Recommendation {
    private JSONObject result;

    /***
     *
     * @return intoarce un obiect de tip JSONObject ce contine rezultatul comenzii.
     */
    public JSONObject getResult() {
        return result;
    }

    /***
     *
     * @param result seteaza campul result al obiectului Recommendation cu obiectul
     *               primit ca si parametru.
     */
    public void setResult(final JSONObject result) {
        this.result = result;
    }

    /***
     *
     * @param action actiunea pe care trebuie sa o executam.
     * @param fileWriter obiectul cu ajutorul caruia cream un JSONObject ce contine
     *                   rezultatul actiunii noastre.
     * @throws IOException exceptie de baza in caz de erori.
     */
    public void execRecommendationTask(final ActionInputData action,
                                       final Writer fileWriter)
            throws IOException {
        if (action.getType().equals(Constants.STANDARD)) {
            result = fileWriter.writeFile(action.getActionId(), "",
                    standardRecommendation(action));
        } else if (action.getType().equals(Constants.BEST_UNSEEN)) {
            result = fileWriter.writeFile(action.getActionId(), "",
                    bestUnseenRecommendation(action));
        } else if (action.getType().equals(Constants.FAVORITE)) {
            result = fileWriter.writeFile(action.getActionId(), "",
                    favoriteRecommendation(action));
        } else if (action.getType().equals(Constants.SEARCH)) {
            result = fileWriter.writeFile(action.getActionId(), "",
                    searchRecommendation(action));
        } else if (action.getType().equals(Constants.POPULAR)) {
            result = fileWriter.writeFile(action.getActionId(), "",
                    popularRecommendation(action));
        }
    }

    /*
        Recommendation
     */

    /***
     *
     * @param action obiectul ce descrie actiunea pe care trebuie sa o executam.
     * @return un string cu rezultatul actiunii.
     */
    public String bestUnseenRecommendation(final ActionInputData action) {
        /*
        Metoda care intoarce cel mai bun video(cu rating-ul cel mai mare) nevazut
     de utilizatorul cu username-ul oferit in action details
        */
        /*
        Vom crea o lista de video-uri sortate descrescator dupa rating-ul lor
         */
        User utilizator = Transfer.searchUserbyUsername(action);
        if (utilizator != null) {
            List<Video> videos = new ArrayList<>();
            videos.addAll(Database.getDatabase().findAllMovies());
            videos.addAll(Database.getDatabase().findAllShows());
            /*
                Sortam lista de video-uri descrescator dupa rating.
             */
            videos.sort((o1, o2) -> Double.compare(o2.getAverageRating(), o1.getAverageRating()));

            for (Video video : videos) {
                /*
                    Imediat cum gasim un video nevazut de utilizator din lista sortata dupa
                 raiting intoarcem mesajul specific.
                 */
                if (wasSeen(utilizator, video.getName()) == 0) {
                    return "BestRatedUnseenRecommendation result: " + video;
                }
            }
        }
        return "BestRatedUnseenRecommendation cannot be applied!";
    }

    /***
     *
     * @param action obiectul ce descrie actiunea pe care trebuie sa o executam.
     * @return intoarce un string cu rezultatul actiunii.
     */
    public String standardRecommendation(final ActionInputData action) {
        /*
            Metoda care intoarce primul video nevazut de un anumit utilizator.
        */
        User utilizator = Transfer.searchUserbyUsername(action);
        if (utilizator != null) {
            List<Video> videos = new ArrayList<>();
            videos.addAll(Database.getDatabase().findAllMovies());
            videos.addAll(Database.getDatabase().findAllShows());
            for (Video video : videos) {
                /*
                    Daca gasim un video care nu a mai fost vazut de utilizator
                 intoarcem mesajul specific recomandarii.
                 */
                if (wasSeen(utilizator, video.getName()) == 0) {
                    return "StandardRecommendation result: " + video;
                }
            }
        }
        return "StandardRecommendation cannot be applied!";
    }


    /***
     *
     * @param action obiectul ce descrie actiunea noastra.
     * @return un string ce contine rezultatul actiunii ce am executat-o.
     */
    public String favoriteRecommendation(final ActionInputData action) {
        /*
            Metoda care intoarce filmul cel mai des intalnit in lista de favorite
        */
        User utilizator = Transfer.searchUserbyUsername(action);
        if (utilizator != null && utilizator.getSubscription().equals("PREMIUM")) {
            List<Video> allVideos = new ArrayList<>();
            List<Video> allVideosSorted;
            allVideos.addAll(Database.getDatabase().findAllMovies());
            allVideos.addAll(Database.getDatabase().findAllShows());

            /*
                Sortam lista de video-uri dupa numarul de aparitii in listele de favorite
             ale utilizatorilor.
             */
            allVideosSorted = Sortings.sortVideosForFavoriteRecommendation(allVideos);

            for (Video video : allVideosSorted) {
                /*
                    Daca gasim un video care nu a mai fost vazut inainte si apare
                 in cel putin o lista de favorite returnam mesajul specific acestei
                 situatii.
                 */
                if (wasSeen(utilizator, video.getName()) == 0
                        && video.getAppearancesInFavorites() != 0) {
                    return "FavoriteRecommendation result: " + video;
                }
            }
        }
        /*
            Daca se ajunge aici inseamna ca recomandarea ceruta nu poate fi executata.
         */
        return "FavoriteRecommendation cannot be applied!";
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
            Metoda care ne va spune daca un video a fost deja notat de catre un utilizator
        */
        if (utilizator.getHistory().containsKey(videoTitle)) {
            return 1;
        } else {
            return 0;
        }
    }

    /***
     *
     * @param action obiectul ce descrie actiunea.
     * @param user utilizatorul pentru care vrem sa cautam video-uri nevazute si ce
     *             au un anumit gen.
     * @return  o lista de video-uri ce au un anumit gen si care nu au mai fost vazut
     * de catre utilizatorul dat ca si parametru
     */
    public List<Video> getVideosWithASpecificGenreAndNotSeen(final ActionInputData action,
                                                             final User user) {
        /*
            Metoda auxiliara care ne extrage toate video-urile din baza de date
        care apartin unui anumit gen specificat in descrierea lui action si care
        nu au mai fost vazute de utilizator inainte.
        */
        Genre gen = Utils.stringToGenre(action.getGenre());
        List<Video> videos = new ArrayList<>();
        if (user != null) {
            for (Film movie : Database.getDatabase().findAllMovies()) {
                if (movie.getGenres().contains(gen) && wasSeen(user, movie.getName()) == 0) {
                    videos.add(movie);
                }
            }
            for (Serial show : Database.getDatabase().findAllShows()) {
                if (show.getGenres().contains(gen) && wasSeen(user, show.getName()) == 0) {
                    videos.add(show);
                }
            }
        }
        return videos;
    }

    /***
     *
     * @param action obiectul ce descrie actiunea pe care trebuie sa o executam.
     * @return rezultatul actiunii noastra dupa cum ni s-a spus in cerinta.
     */
    public String searchRecommendation(final ActionInputData action) {
        /*
            Metoda care intoarce toate video-urile nevazute de un utilizator
        dintr-un anumit gen dat ca filtru in action.
        */
        User user = Transfer.searchUserbyUsername(action);
        if (user != null && user.getSubscription().equals("PREMIUM")) {
            List<Video> videos = getVideosWithASpecificGenreAndNotSeen(action, user);
            /*
                Avem toate video-urile care apartin genului cerut, acum ne ramane sa le
            sortam crescator dupa rating si dupa nume(al doilea criteriu).
            */
            if (!videos.isEmpty()) {
                videos.sort(Comparator.comparing(Video::getAverageRating)
                        .thenComparing(Video::getName)
                );
                return "SearchRecommendation result: " + videos;
            } else {
                return "SearchRecommendation cannot be applied!";
            }
        }
        return "SearchRecommendation cannot be applied!";
    }

    /***
     *
     * @param unsortedMap o mapa cu cheia de tip Genre si valoare Integer.
     * @return aceeasi mapa dar de aceasta data sortata dupa campul value.
     */
    public Map<Genre, Integer> sortByValue(final Map<Genre, Integer> unsortedMap) {
        /*
            Metoda prin intermediul careia sortam descrescator mapa de popularitate
         in functie de campurile cu valori.
         */
        List<Map.Entry<Genre, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
        list.sort((o1, o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return -1;
            } else if (o1.getValue() < o2.getValue()) {
                return 1;
            }
            return 0;
        });

        Map<Genre, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Genre, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /***
     *
     * @param allVideos o lista cu toate video-urile din baza de date.
     * @return intoarce o mapa cu popularitatea genurilor(fiecare intrare din mapa
     * contine cate un gen si popularitatea acestuia).
     */
    public Map<Genre, Integer> popularityMap(final List<Video> allVideos) {
        /*
            Pentru a vedea cat de populat este un gen am facut o mapa care pe pozitia de chei
         are un gen si pe pozitia de value popularitatea acestui gen(de cate ori a fost vazut
         genul respectiv prin intermediul video-urilor care il reprezinta).
         */
        Map<Genre, Integer> popularity = new HashMap<>();
        /*
            Pentru a calcula popularitatea unui gen parcurgem toate video-urile din baza de date
        si pentru fiecare video parcurgem lista sa de genuri  si actualizam valoarea intrarii
        ce are ca si cheie genul respectiv cu suma dintre valoarea veche a intrarii si
        numarul de vizualizari ale video-ului respectiv.
         */
        for (Video video : allVideos) {
            for (Genre gen : video.getGenres()) {
                /*
                    Cazul in care mapa de popularitate contine deja genul.
                    In acest caz trebuie sa actualizam valoarea asociata cheii.
                 */
                if (popularity.containsKey(gen)) {
                    int oldValue = popularity.get(gen);
                    int newValue = oldValue + video.getNrOfViews();
                    popularity.replace(gen, oldValue, newValue);
                } else if (!popularity.containsKey(gen)) {
                    /*
                        Inseamna ca genul nu exista inainte in mapa de popularitate asa ca
                     vom crea o noua intrare cu cheia reprezentata de acest gen si valoarea
                     reprezentata numarul de vizualizari ale video-ului curent.
                     */
                    popularity.put(gen, video.getNrOfViews());
                }
            }
        }
        return popularity;
    }

    /***
     *
     * @param allVideos lista cu video-uri.
     * @param gen genul pe care trebuie sa il reprezinte video-ul.
     * @param user user-ul pentru care trebuie sa gasim video-ul.
     * @return video-ul nevazut de user-ul primit ca parametru si care reprezinta
     * genul oferit tot ca parametru al functiei.
     */
    public Video firstVideoUnseenWithMostPopularGenre(final List<Video> allVideos,
                                                      final Genre gen, final User user) {
        /*
            Cautam un video in toata baza de date care sa contina genul dat ca parametru
        si sa nu fi fost vazut inainte de utilizatorul dat si el ca parametru al functiei.
         */
        for (Video video : allVideos) {
            if (video.getGenres().contains(gen) && wasSeen(user, video.getName()) == 0) {
                return video;
            }
        }
        /*
            Daca se ajunge aici inseamna ca nu a fost gasit un video cu cerintele noastre.
         */
        return null;
    }

    /***
     *
     * @param action obiectul ce descrie actiunea pe care trebuie sa o realizam.
     * @return un string cu rezultatul actiunii efectuate.
     */
    public String popularRecommendation(final ActionInputData action) {
        /*
            Metoda care intoarce primul video nevizualizat din cel mai popular gen
        */
        User user = Transfer.searchUserbyUsername(action);
        /*
            Cream o lista cu toate video-urile din baza de date.
         */
        List<Video> allVideos = new ArrayList<>();
        allVideos.addAll(Database.getDatabase().findAllMovies());
        allVideos.addAll(Database.getDatabase().findAllShows());

        /*
            Cream popularity map-ul cu ajutorul metodei definite mai sus.
         */
        Map<Genre, Integer> popularity = popularityMap(allVideos);
        /*
            Sortam popularity map-ul descrescator pentru a avea pe primele
         intrari cele mai populare genuri.
         */
        Map<Genre, Integer> sortedPopularityMap = sortByValue(popularity);

        if (user != null) {
            if (user.getSubscription().equals("PREMIUM")) {
                for (Map.Entry<Genre, Integer> entry : sortedPopularityMap.entrySet()) {
                    /*
                        Ne folosim de functia care ne intoarce primul video nevazut de
                    utilizator caruia ii dam ca si parametru cel mai popular gen gasit.
                     */
                    Video video = firstVideoUnseenWithMostPopularGenre(allVideos,
                            entry.getKey(), user);
                    /*
                        Daca am gasit un video cu cerintele date atunci il returnam, altfel
                     continuam loop-ul pentru urmatorul cel mai popular gen. Facem asa pana
                     cand gasim un video care sa nu fie vazut de catre utilizator.
                     */
                    if (video != null) {
                        return "PopularRecommendation result: " + video;
                    }
                }
            }
        }
        /*
            Daca am ajuns aici inseamna ca nu s-a putut gasi un video care sa
        poata fi recomandat utilizatorului si returnam rezultatul specific actiunii.
         */
        return "PopularRecommendation cannot be applied!";
    }
}
