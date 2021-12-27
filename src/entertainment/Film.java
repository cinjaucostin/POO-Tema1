package entertainment;

import java.util.ArrayList;
import java.util.List;

public class Film extends Video {
    private int duration;
    private List<String> actors;
    private List<Double> ratings;

    public Film(final String name, final int year, final List<Genre> genres,
                final int duration, final List<String> actors) {
        super(name, year, genres);
        this.duration = duration;
        this.actors = actors;
        this.ratings = new ArrayList<>();
    }

    public Film() {
        this(null, 0, null, 0, null);
    }

    /***
     *
     * @param film filmul caruia dorim sa ii facem o copie
     */
    public Film(final Film film) {
        this.setName(film.getName());
        this.setYear(film.getYear());
        this.setGenres(film.getGenres());
        this.duration = film.duration;
        this.actors = film.actors;
        this.ratings = film.ratings;
    }

    /***
     *
     * @return intoarce rating-ul mediu al filmului.
     */
    @Override
    public double getAverageRating() {
        if (this.ratings.size() == 0) {
            return 0;
        }
        double s = 0;
        for (Double rating : this.ratings) {
            s = s + rating;
        }
        return s / this.ratings.size();
    }

    /***
     *
     * @return intoarce durata filmului.
     */
    @Override
    public int getDurationSum() {
        return getDuration();
    }

    /***
     *
     * @return o lista de elemente double cu toate rating-urile primite de film.
     */
    public List<Double> getRatings() {
        return ratings;
    }

    /***
     *
     * @param ratings seteaza campul ratings cu o lista de elemente double primita
     *                ca si parametru.
     */
    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }

    /***
     *
     * @return intoarce durata filmului.
     */
    public int getDuration() {
        return duration;
    }

    /***
     *
     * @param duration seteaza durata filmului dupa parametrul primit.
     */
    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /***
     *
     * @return o lista cu numele actorilor care au jucat in film.
     */
    public List<String> getActors() {
        return actors;
    }

    /***
     *
     * @param actors seteaza lista cu actorii care au jucat in film.
     */
    public void setActors(final List<String> actors) {
        this.actors = actors;
    }
}
