package utils;

import actor.Actor;
import common.Constants;
import entertainment.Video;
import user.User;

import java.util.Comparator;
import java.util.List;

public final class Sortings {
    private Sortings() {
    }

    /*
        Metode de sortare pentru liste de actori
     */

    /***
     *
     * @param allActors o lista de actori de trebuie sortati
     * @param sortType tipul de sortare: crescatoare sau descrescatoare("asc" sau "desc")
     * @return lista de actori sortata dupa urmatoarele criterii: medie si nume
     */
    public static List<Actor> sortActorsByAverageAndName(final List<Actor> allActors,
                                                         final String sortType) {
        if (sortType.equals(Constants.ASC)) {
            allActors.sort(Comparator.comparingDouble(Actor::getAverage)
                    .thenComparing((o1, o2) -> {
                        if (o1.getName().compareTo(o2.getName()) > 0) {
                            return 1;
                        } else if (o1.getName().compareTo(o2.getName()) < 0) {
                            return -1;
                        }
                        return 0;
                    }));
        } else if (sortType.equals(Constants.DESC)) {
            allActors.sort((o1, o2) -> {
                if (o1.getAverage() > o2.getAverage()) {
                    return -1;
                } else if (o1.getAverage() < o2.getAverage()) {
                    return 1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) > 0) {
                        return -1;
                    } else if (o1.getName().compareTo(o2.getName()) < 0) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        return allActors;
    }

    /***
     *
     * @param allActors lista de actori ce trebuie sa fie sortata
     * @param sortType tipul de sortare("asc" sau "desc")
     * @return lista de actori sortata dupa urmatoarele criterii: numarul de awards si nume
     */
    public static List<Actor> sortActorsByTotalAwardsAndName(final List<Actor> allActors,
                                                             final String sortType) {
        if (sortType.equals(Constants.ASC)) {
            allActors.sort((o1, o2) -> {
                if (o1.getTotalOfAwards() < o2.getTotalOfAwards()) {
                    return -1;
                } else if (o1.getTotalOfAwards() > o2.getTotalOfAwards()) {
                    return 1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) > 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (sortType.equals(Constants.DESC)) {
            allActors.sort((o1, o2) -> {
                if (o1.getTotalOfAwards() < o2.getTotalOfAwards()) {
                    return 1;
                } else if (o1.getTotalOfAwards() > o2.getTotalOfAwards()) {
                    return -1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) < 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) > 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        return allActors;
    }

    /***
     *
     * @param allActors lista de actori ce trebuie sortata
     * @param sortType tipul de sortare
     * @return lista de actori sortata de aceasta data doar dupa nume
     */
    public static List<Actor> sortActorsByName(final List<Actor> allActors,
                                               final String sortType) {
        if (sortType.equals(Constants.ASC)) {
            allActors.sort(Comparator.comparing(Actor::getName));
        } else if (sortType.equals(Constants.DESC)) {
            allActors.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        }
        return allActors;
    }

    /***
     *
     * @param allVideos lista de video-uri ce va trebui sortata
     * @param sortType tipul de sortare
     * @return lista de video-uri sortata dupa criteriile: rating si nume
     */
    public static List<Video> sortVideoByRatingAndName(final List<Video> allVideos,
                                                       final String sortType) {
        if (sortType.equals(Constants.ASC)) {
            allVideos.sort(
                    Comparator.comparingDouble(Video::getAverageRating)
                            .thenComparing((o1, o2) -> {
                                if (o1.getName().compareTo(o2.getName()) > 0) {
                                    return 1;
                                } else if (o1.getName().compareTo(o2.getName()) < 0) {
                                    return -1;
                                }
                                return 0;
                            }));
        } else if (sortType.equals(Constants.DESC)) {
            allVideos.sort((o1, o2) -> {
                if (o1.getAverageRating() < o2.getAverageRating()) {
                    return 1;
                } else if (o1.getAverageRating() > o2.getAverageRating()) {
                    return -1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) < 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) > 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        return allVideos;
    }

    /***
     *
     * @param allVideos lista de video-uri
     * @param sortType tipul de sortare
     * @return lista de video-uri sortata dupa urmatoarele criterii: numar de vizualizari si nume
     */
    public static List<Video> sortVideoByViewsAndName(final List<Video> allVideos,
                                                      final String sortType) {
        if (sortType.equals(Constants.ASC)) {
            allVideos.sort(Comparator.comparingDouble(Video::getNrOfViews)
                    .thenComparing((o1, o2) -> {
                        if (o1.getName().compareTo(o2.getName()) > 0) {
                            return 1;
                        } else if (o1.getName().compareTo(o2.getName()) < 0) {
                            return -1;
                        }
                        return 0;
                    }));
        } else if (sortType.equals(Constants.DESC)) {
            allVideos.sort((o1, o2) -> {
                if (o1.getNrOfViews() < o2.getNrOfViews()) {
                    return 1;
                } else if (o1.getNrOfViews() > o2.getNrOfViews()) {
                    return -1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) < 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) > 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        return allVideos;
    }

    /***
     *
     * @param allVideos lista de video-uri
     * @param sortType tipul de sortare
     * @return lista de video-uri sortata dupa durata si apoi dupa nume
     */
    public static List<Video> sortVideoByDurationAndName(final List<Video> allVideos,
                                                         final String sortType) {
        if (sortType.equals(Constants.ASC)) {
            allVideos.sort((o1, o2) -> {
                if (o1.getDurationSum() < o2.getDurationSum()) {
                    return -1;
                } else if (o1.getDurationSum() > o2.getDurationSum()) {
                    return 1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) > 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (sortType.equals(Constants.DESC)) {
            allVideos.sort((o1, o2) -> {
                if (o1.getDurationSum() > o2.getDurationSum()) {
                    return -1;
                } else if (o1.getDurationSum() < o2.getDurationSum()) {
                    return 1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) < 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) > 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        return allVideos;
    }

    /***
     *
     * @param allVideos lista de video-uri
     * @param sortType tipul de sortare
     * @return lista de video-uri sortata dupa: numarul de aparitii in listele de favorite si nume
     */
    public static List<Video> sortVideoByFavoritesAndName(final List<Video> allVideos,
                                                          final String sortType) {
        if (sortType.equals(Constants.ASC)) {
            allVideos.sort((o1, o2) -> {
                if (o1.getAppearancesInFavorites() < o2.getAppearancesInFavorites()) {
                    return -1;
                } else if (o1.getAppearancesInFavorites() > o2.getAppearancesInFavorites()) {
                    return 1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) > 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (sortType.equals(Constants.DESC)) {
            allVideos.sort((o1, o2) -> {
                if (o1.getAppearancesInFavorites() > o2.getAppearancesInFavorites()) {
                    return -1;
                } else if (o1.getAppearancesInFavorites() < o2.getAppearancesInFavorites()) {
                    return 1;
                } else {
                    if (o1.getName().compareTo(o2.getName()) < 0) {
                        return 1;
                    } else if (o1.getName().compareTo(o2.getName()) > 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        return allVideos;
    }

    /***
     *
     * @param allUsers lista de utilizatori
     * @param sortType tipul de sortare
     * @return lista de useri sortati dupa numarul de rating-uri pe care l-au oferit si nume
     */
    public static List<User> sortUserByRatingAndName(final List<User> allUsers,
                                                     final String sortType) {
        if (sortType.equals(Constants.ASC)) {
            allUsers.sort((o1, o2) -> {
                if (o1.getRatingsSize() < o2.getRatingsSize()) {
                    return -1;
                } else if (o1.getRatingsSize() > o2.getRatingsSize()) {
                    return 1;
                } else {
                    if (o1.getUsername().compareTo(o2.getUsername()) > 0) {
                        return 1;
                    } else if (o1.getUsername().compareTo(o2.getUsername()) < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (sortType.equals(Constants.DESC)) {
            allUsers.sort((o1, o2) -> {
                if (o1.getRatingsSize() < o2.getRatingsSize()) {
                    return 1;
                } else if (o1.getRatingsSize() > o2.getRatingsSize()) {
                    return -1;
                } else {
                    if (o1.getUsername().compareTo(o2.getUsername()) < 0) {
                        return 1;
                    } else if (o1.getUsername().compareTo(o2.getUsername()) > 0) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        return allUsers;
    }

    /***
     *
     * @param allVideos lista cu video-uri.
     * @return lista de video-uri sortata descrescator dupa numarul de aparitii in listele
     * de favorite.
     */
    public static List<Video> sortVideosForFavoriteRecommendation(final List<Video> allVideos) {
        allVideos.sort((o1, o2) -> {
            if (o1.getAppearancesInFavorites() > o2.getAppearancesInFavorites()) {
                return -1;
            } else if (o1.getAppearancesInFavorites() < o2.getAppearancesInFavorites()) {
                return 1;
            }
            return 0;
        });
        return allVideos;
    }
}
