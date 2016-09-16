package org.muganga.utilities;


public class EndPoints {


    public static final String BASE_URL = "http://www.myapifilms.com/imdb/";
    public static final String URL_IN_THEATERS = "http://www.myapifilms.com/imdb/inTheaters?token=a052ec23-2d0d-4c3d-aeed-eae6cb149393";//?end=30";
    public static final String URL_COMING_SOON = "http://www.myapifilms.com/imdb/inTheaters?token=a052ec23-2d0d-4c3d-aeed-eae6cb149393";
    //"http://www.myapifilms.com/imdb/comingSoon?token=a052ec23-2d0d-4c3d-aeed-eae6cb149393";

    public static final String URL_TOP_MOVIES = "http://www.myapifilms.com/imdb/inTheaters?token=a052ec23-2d0d-4c3d-aeed-eae6cb149393";
    //"http://www.myapifilms.com/imdb/top?token=a052ec23-2d0d-4c3d-aeed-eae6cb149393;//&data=F&end=30";
    public static final String URL_BOTTOM_MOVIES ="http://www.myapifilms.com/imdb/inTheaters?token=a052ec23-2d0d-4c3d-aeed-eae6cb149393";
    // "http://www.myapifilms.com/imdb/bottom?token=a052ec23-2d0d-4c3d-aeed-eae6cb149393;//&data=F&end=30";
    public static final String URL_MOVIE = "http://www.myapifilms.com/imdb?token=a052ec23-2d0d-4c3d-aeed-eae6cb149393&title= ";

    public static final String WITH_TRAILER = " &trailer=1";

    private static final String URL = "http://www.myapifilms.com/imdb";


    public static String getRequestUrlInTheatersMovies() {

        return URL_IN_THEATERS;

    }

    public static String getRequestUrlTopMovies() {

        return URL_TOP_MOVIES;

    }

    public static String getRequestUrlBottomMovies() {

        return URL_BOTTOM_MOVIES;

    }

    public static String getRequestUrlFoundMovies(String title, String limit) {


        return (URL_MOVIE
                + title
                + limit
                + WITH_TRAILER).replaceAll("\\s+", "");
    }

    public static String getRequestUrlComingSoon() {

        return URL_COMING_SOON;

    }

    public static String getRequestMovieUrl(String title) {


        return (URL_MOVIE
                + title
                + WITH_TRAILER).replaceAll("\\s+", "");

    }


}
