package actor;

import entertainment.Video;
import utils.Transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography, final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public Actor() {
        this(null, null, null, null);
    }

    /***
     *
     * @param actor actorul caruia dorim sa ii facem o copie
     */
    public Actor(final Actor actor) {
        this.name = actor.name;
        this.careerDescription = actor.careerDescription;
        this.filmography = actor.filmography;
        this.awards = actor.awards;
    }

    /***
     *
     * @return numarul de premii castigate ale actorului.
     */
    public int getTotalOfAwards() {
        int s = 0;
        for (Map.Entry<ActorsAwards, Integer> entry : awards.entrySet()) {
            s = s + entry.getValue();
        }
        return s;
    }

    /***
     *
     * @return intoarce o lista cu toate video-urile in care actorul a jucat pe parcursul
     * carierei sale.
     */
    public List<Video> getListOfVideosFromFilmography() {
        List<Video> videouriJucate = new ArrayList<>();
        for (String video : filmography) {
            Video videoInCareAJucat = Transfer.searchVideoByTitle(video);
            if (videoInCareAJucat != null) {
                videouriJucate.add(videoInCareAJucat);
            }
        }
        return videouriJucate;
    }

    /***
     *
     * @return intoarce media acestuia calculata dupa regulile specificate in cerinta.
     */
    public double getAverage() {
        int i = 0;
        double s = 0;
        List<Video> videouriJucate = getListOfVideosFromFilmography();
        for (Video video : videouriJucate) {
            if (video.getAverageRating() != 0) {
                s += video.getAverageRating();
            } else {
                i++;
            }
        }
        if (videouriJucate.size() != i) {
            return s / (videouriJucate.size() - i);
        } else {
            return 0;
        }
    }

    /***
     *
     * @return getter ce intoarce numele actorului.
     */
    public String getName() {
        return name;
    }

    /***
     *
     * @return getter ce intoarce descrierea carierei actorului.
     */
    public String getCareerDescription() {
        return careerDescription;
    }

    /***
     *
     * @return getter ce intoarce o lista cu toate numele filmelor in
     * care actorul a jucat.
     */
    public List<String> getFilmography() {
        return filmography;
    }

    /***
     *
     * @return intoarce un obiect de tip Map ce contine ca si cheie un anumit award si
     * ca valoare de cate ori a castigat actorul award-ul respectiv.
     */
    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    /***
     *
     * @return numele actorului, aceasta metoda ne va fi utila la partea de Queries.
     */
    @Override
    public String toString() {
        return name;
    }
}
