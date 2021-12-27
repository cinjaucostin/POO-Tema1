package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String username;
    private String subscription;
    private Map<String, Integer> history;
    private ArrayList<String> favourite;
    private Map<String, Double> ratings;

    public User(final String username, final String subscripiton,
                final Map<String, Integer> history, final ArrayList<String> favourite) {
        this.username = username;
        this.subscription = subscripiton;
        this.history = history;
        this.favourite = favourite;
        this.ratings = new HashMap<>();
    }

    public User() {
        this(null, null, null, null);
    }

    /***
     *
     * @param user utilizatorul caruia dorim sa ii facem o copie.
     */
    public User(final User user) {
        this.username = user.username;
        this.subscription = user.subscription;
        this.history = user.history;
        this.favourite = user.favourite;
        this.ratings = user.ratings;
    }

    /***
     *
     * @return intoarce dimensiunea listei de rating-uri.
     */
    public int getRatingsSize() {
        return this.ratings.size();
    }

    /***
     *
     * @return intoarce username-ul utilizatorului.
     */
    public String getUsername() {
        return username;
    }

    /***
     *
     * @param username seteaza username-ul utilizatorului cu cel primit ca si parametru.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /***
     *
     * @return intoarce tipul de abonament al utilizatorului.
     */
    public String getSubscription() {
        return subscription;
    }

    /***
     *
     * @param subscription seteaza tipul de abonament al utilizatorului cu cel
     *                     primit ca si parametru.
     */
    public void setSubscription(final String subscription) {
        this.subscription = subscription;
    }

    /***
     *
     * @return intoarce un obiect Map care are ca si cheie numele unui video
     * si ca si valoare numarul de vizualizari ale utilizatorului pentru acel video.
     */
    public Map<String, Integer> getHistory() {
        return history;
    }

    /***
     *
     * @param history seteaza istoricul utilizatorului cu cel primit ca si parametru.
     */
    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }

    /***
     *
     * @return intoarce o lista de string-uri ce reprezinta numele video-urilor favorite
     * ale utilizatorului.
     */
    public List<String> getFavourite() {
        return favourite;
    }

    /***
     *
     * @param favourite primeste o lista de string-uri cu numele video-urilor favorite si o
     *                  seteaza utilizatorului.
     */
    public void setFavourite(final ArrayList<String> favourite) {
        this.favourite = favourite;
    }

    /***
     *
     * @return intoarce un obiect de tip Map in care cheia reprezinta numele unui video
     * si valoarea rating-ul utilizatorului pentru acel video.
     */
    public Map<String, Double> getRatings() {
        return ratings;
    }

    /***
     *
     * @param ratings o mapa de rating-uri si o seteaza campului utilizatorului.
     */
    public void setRatings(final Map<String, Double> ratings) {
        this.ratings = ratings;
    }

    /***
     *
     * @return intoarce numele utilizatorului, ne va fi utila la Queries.
     */
    @Override
    public String toString() {
        return username;
    }
}
