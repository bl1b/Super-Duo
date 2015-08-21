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

package de.gruenewald.android.util;

/**
 * Created by jan on 21.08.2015.
 */
public class Strings {

    public static boolean isEmpty(String pString) {
        return (pString == null || pString.trim().length() == 0);
    }

    public static boolean isNotEmpty(String pString) {
        return !isEmpty(pString);
    }
}
