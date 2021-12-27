package entertainment;

import java.util.List;

public class Video {
    private String name;
    private int year;
    private List<Genre> genres;
    /*
        //Camp folosit pentru a contoriza numarul de vizualizari ale unui anumit video
     */
    private int nrOfViews = 0;
    /*
        Camp folosit pentru a contoriza numarul de aparitii in listele de favorite ale
    utilizatorilor ale unui anumit video
     */
    private int appearancesInFavorites = 0;

    public Video(final String name, final int year, final List<Genre> genres) {
        this.name = name;
        this.year = year;
        this.genres = genres;
    }

    public Video() {
        this(null, 0, null);
    }

    /***
     *
     * @return metoda va fi suprascrisa in subclase
     */
    public double getAverageRating() {
        return 0;
    }

    /***
     *
     * @return de asemenea, metoda va fi suprascrisa in subclase
     */
    public int getDurationSum() {
        return 0;
    }


    /***
     *
     * @param videoContent un alt video a carui copie trebuie facuta
     */
    public Video(final Video videoContent) {
        this.name = videoContent.name;
        this.year = videoContent.year;
        this.genres = videoContent.genres;
        this.nrOfViews = videoContent.getNrOfViews();
        this.appearancesInFavorites = videoContent.getAppearancesInFavorites();
    }

    /***
     *
     * @return numarul de vizualizari ale video-ului.
     */
    public int getNrOfViews() {
        return nrOfViews;
    }

    /***
     *
     * @param nrOfViews seteaza numarul de vizualizari ale video-ului.
     */
    public void setNrOfViews(final int nrOfViews) {
        this.nrOfViews = nrOfViews;
    }

    /***
     *
     * @return numarul de aparitii in listele de favorite ale utilizatorilor.
     */
    public int getAppearancesInFavorites() {
        return appearancesInFavorites;
    }

    /***
     *
     * @param appearancesInFavorites actualizeaza campul cu numarul de aparitii in listele
     *                               de favorite ale utilizatorilor.
     */
    public void setAppearancesInFavorites(final int appearancesInFavorites) {
        this.appearancesInFavorites = appearancesInFavorites;
    }

    /***
     *
     * @return numele video-ului.
     */
    public String getName() {
        return name;
    }

    /***
     *
     * @param name seteaza numele video-ului cu cel primit ca si parametru.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /***
     *
     * @return anul in care a aparut video-ul.
     */
    public int getYear() {
        return year;
    }

    /***
     *
     * @param year seteaza anul in care a aparut video-ul cu cel primit ca parametru.
     */
    public void setYear(final int year) {
        this.year = year;
    }

    /***
     *
     * @return o lista de obiecte de tip Genre, reprezentand genurile video-ului.
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /***
     *
     * @param genres seteaza lista de genuri a video-ului cu cea primita ca parametru.
     */
    public void setGenres(final List<Genre> genres) {
        this.genres = genres;
    }

    /***
     *
     * @return numele video-ului, aceasta metoda ne va fi utila in partea de Queries a temei.
     */
    @Override
    public String toString() {
        return name;
    }
}
