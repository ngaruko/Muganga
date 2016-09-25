package org.muganga.utilities;

/**
 * Created by itl on 9/07/2015.
 */
public class MovieSorter {
    public static class Sort{

        public static String sortString="";

        public static String getSortString() {
            return sortString;
        }

        public static void setSortString(String sortString) {
            Sort.sortString = sortString;
        }
    }

    public static class Filter{

        public static String filterString="";

        public static String getFilterString() {
            return filterString;
        }

        public static void setFilterString(String filterString) {
            Filter.filterString = filterString;
        }
    }
}
