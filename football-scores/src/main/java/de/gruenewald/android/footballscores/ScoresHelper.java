/*
 * *
 *  * ****************************************************************************
 *  * Copyright (c) 2015 by Jan Grünewald.
 *  * jan.gruenewald84@googlemail.com
 *  * <p>
 *  * This file is part of 'Super Duo'. 'Super Duo' was developed as
 *  * part of the Android Developer Nanodegree by Udacity.
 *  * <p>
 *  * 'Super Duo' is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  * <p>
 *  * 'Super Duo' is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  * <p>
 *  * You should have received a copy of the GNU General Public License
 *  * along with 'Super Duo'.  If not, see <http://www.gnu.org/licenses/>.
 *  * ****************************************************************************
 *
 */

package de.gruenewald.android.footballscores;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

/**
 * Class ScoresHelper.
 *
 * [DOCUMENT ME]
 *
 * @author Jan Grünewald
 * @version 1.0.0
 */
public class ScoresHelper {
    private static final String LOG_TAG = ScoresHelper.class.getSimpleName();

    /**
     * Creates the spannable (share-icon + text) to be used as label for the share-button
     * on each scores_list_item
     *
     * @param pContext The context to use.
     * @param pLabelId The resource-identifier for the label to use
     * @param pImageId The resource-identifier for the image to use
     * @return The spannable string or <code>null</code> is something went wrong.
     */
    public static Spannable createLeftImageSpannable(Context pContext, int pLabelId, int pImageId) {
        Spannable myResult = null;
        final String myLabel = "  " + pContext.getString(pLabelId);
        final Drawable myIcon = getDrawableCompat(pContext, pImageId);

        if (pContext != null && myIcon != null) {
            myResult = new SpannableString(myLabel);
            // ideal case: myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
            // unfortunately for some reason this doesn't work properly on a button for example
            myIcon.setBounds(0, 0, 50, 50);
            ImageSpan myIconSpan = new ImageSpan(myIcon, ImageSpan.ALIGN_BOTTOM);
            myResult.setSpan(myIconSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return myResult;
    }

    /**
     * @param pContext    The context in which the drawable should be found.
     * @param pDrawableId The resource identifier for the drawable to be obtained.
     * @return The resulting drawable or <code>null</code> if the resource could not be found.
     */
    public static Drawable getDrawableCompat(Context pContext, int pDrawableId) {
        Drawable myResult = null;

        if (pContext != null && pDrawableId > 0) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    myResult = pContext.getResources().getDrawable(pDrawableId, pContext.getTheme());
                } else {
                    // we can ignore the deprecation since we are checking the version with the
                    // if-clause
                    //noinspection deprecation
                    myResult = pContext.getResources().getDrawable(pDrawableId);
                }
            } catch (Resources.NotFoundException e) {
                Log.w(LOG_TAG, "Drawable resource with id '" + pDrawableId + "' not found");
                myResult = null;
            }
        }
        return myResult;
    }
}
