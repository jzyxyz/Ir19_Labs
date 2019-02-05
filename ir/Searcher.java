/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   Johan Boye, 2017
 */

package ir;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.ListIterator;

import ir.Query.QueryTerm;

/**
 * Searches an index for results of a query.
 */
public class Searcher {

    /** The index to be searched by this Searcher. */
    Index index;

    /** The k-gram index to be searched by this Searcher */
    KGramIndex kgIndex;

    /** Constructor */
    public Searcher(Index index, KGramIndex kgIndex) {
        this.index = index;
        this.kgIndex = kgIndex;
    }

    /**
     * Searches the index for postings matching the query.
     * 
     * @return A postings list representing the result of the query.
     */
    public PostingsList search(Query query, QueryType queryType, RankingType rankingType) {
        //
        // REPLACE THE STATEMENT BELOW WITH YOUR CODE
        //
        PostingsList result = new PostingsList();
        int n_terms = query.size();
        if (n_terms == 0)
            return result;
        // map the terms to a new PostingsList array
        ArrayList<PostingsList> pl_all = new ArrayList<PostingsList>();
        for (int i = 0; i < n_terms; i++) {
            PostingsList pl_i = index.getPostings(query.getTermStringAt(i));
            if (pl_i != null)
                pl_all.add(pl_i);
        }
        if (pl_all.size() == 0) // term not indexed
            return result;

        result = pl_all.get(0);
        if (n_terms == 1) // just one, on need to intersect or phrase
            return result.intersectWith(result);

        pl_all.remove(0);

        switch (queryType) {
        case INTERSECTION_QUERY:

            for (PostingsList pl : pl_all) {
                result = result.intersectWith(pl);
            }

            break;

        case PHRASE_QUERY:

            for (PostingsList pl : pl_all) {
                result = result.phraseWith(pl);
            }

            break;
        case RANKED_QUERY:
            break;

        }
        // Index.showDocInfo(result);
        return result;
    }
}