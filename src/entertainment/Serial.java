package entertainment;

import java.util.List;

public class Serial extends Video {
    private List<String> cast;
    private final int numberOfSeasons;
    private List<Season> seasons;

    public Serial(final String name, final int year, final List<Genre> genres,
                  final List<String> cast, final int numberOfSeasons,
                  final List<Season> seasons) {
        super(name, year, genres);
        this.cast = cast;
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public Serial() {
        this(null, 0, null, null, 0, null);
    }

    /***
     *
     * @return intoarce rating-ul mediu al serialului calculat dupa regula data
     * in cerinta.
     */
    @Override
    public double getAverageRating() {
        double mediumRatingOnAllSeasons = 0;
        for (Season season : this.seasons) {
            double s = 0;
            if (season.getRatings().size() != 0) {
                for (Double rating : season.getRatings()) {
                    s = s + rating;
                }
                s = s / season.getRatings().size();
            }
            mediumRatingOnAllSeasons += s;
        }
        return mediumRatingOnAllSeasons / this.seasons.size();
    }

    /***
     *
     * @return intoarce suma duratelor tuturor sezoanelor serialului.
     */
    @Override
    public int getDurationSum() {
        int s = 0;
        for (Season sezon : seasons) {
            s = s + sezon.getDuration();
        }
        return s;
    }

    /***
     *
     * @param serial serialul caruia dorim sa ii facem o copie
     */
    public Serial(final Serial serial) {
        this.setName(serial.getName());
        this.setYear(serial.getYear());
        this.setGenres(serial.getGenres());
        this.cast = serial.cast;
        this.numberOfSeasons = serial.numberOfSeasons;
        this.seasons = serial.seasons;
    }

    /***
     *
     * @return returneaza o lista cu numele fiecarui actor din cast-ul serialului.
     */
    public List<String> getCast() {
        return cast;
    }

    /***
     *
     * @param cast seteaza lista de cast a serialului cu cea primita ca parametru.
     */
    public void setCast(final List<String> cast) {
        this.cast = cast;
    }

    /***
     *
     * @return o lista cu toate sezoanele serialului.
     */
    public List<Season> getSeasons() {
        return seasons;
    }

    /***
     *
     * @param seasons o lista de sezoane pe care o vom pune in lista cu sezoane a
     *                serialului nostru.
     */
    public void setSeasons(final List<Season> seasons) {
        this.seasons = seasons;
    }

}
