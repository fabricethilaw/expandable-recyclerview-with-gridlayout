package com.armani2015.android.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thilaw Fabrice on 2016-05-23.
 */
public class ListChopper {


    /**
     * Splits a list into non-view sublists of length L
     *
     * @param list - the main list to be split
     * @param L    - the maximum size of each sublist. Last sublist may a lesser size
     * @param <T>  the type of the items in the main list and sublists
     * @return A list of sublists
     */
    public static <T> List<List<T>> splitListBySize(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    /**
     * /** Splits a list into non-view iParts of sublists
     *
     * @param list   - the main list to be split
     * @param <T>    the type of the items in the main list and sublists
     * @param iParts - the expected numbers of sublists we may get after split
     * @return A list of sublists
     */
    public static <T> List<List<T>> splitListByParts(final List<T> list, final int iParts) {
        final List<List<T>> lsParts = new ArrayList<List<T>>();
        final int iChunkSize = list.size() / iParts;
        int iLeftOver = list.size() % iParts;
        int iTake = iChunkSize;

        for (int i = 0, iT = list.size(); i < iT; i += iTake) {
            if (iLeftOver > 0) {
                iLeftOver--;

                iTake = iChunkSize + 1;
            } else {
                iTake = iChunkSize;
            }

            lsParts.add(new ArrayList<T>(list.subList(i, Math.min(iT, i + iTake))));
        }

        return lsParts;
    }
}
