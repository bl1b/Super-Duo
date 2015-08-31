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

package de.gruenewald.android.alexandria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import it.jaschke.alexandria.R;

public class BarcodeScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
