/*
 This file is part of Privacy Friendly Reckoning Skills.
 Privacy Friendly Reckoning Skills is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.
 Privacy Friendly Reckoning Skills is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Reckoning Skills. If not, see <http://www.gnu.org/licenses/>.
 */

package osyoos.sofeeg.oukop.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import osyoos.sofeeg.oukop.R;
import osyoos.sofeeg.oukop.database.PFASQLiteHelper;
import osyoos.sofeeg.oukop.exerciseInstance;
import osyoos.sofeeg.oukop.gameInstance;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity   {

    TextView solved;

    String playerName;
    gameInstance game;

    ArrayList<TextView> resultTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        SharedPreferences hs = this.getSharedPreferences("pfa-math-highscore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = hs.edit();
        editor.putBoolean("continue", false);
        editor.commit();

        game = (gameInstance) getIntent().getSerializableExtra("game");
        playerName = getIntent().getStringExtra("name");


        solved = (TextView) findViewById(R.id.solved);





        for (int i = 0; i < 10; i++) {

            TextView exercise = new TextView(this);
            TextView solution = new TextView(this);
            TextView equalTo = new TextView(this);

            exercise.setTextSize(28);
            solution.setTextSize(28);
            equalTo.setTextSize(28);

            exercise.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            exercise.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            solution.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


            if (game.exercises.get(i).revisit) {
                exercise.setTextColor(getResources().getColor(R.color.middlegrey));
            } else {
                exercise.setTextColor(getResources().getColor(R.color.lightblue));
            }
            solution.setTextColor(getResources().getColor(R.color.red));


            String exerciseSt =  game.exercises.get(i).x + " " + game.exercises.get(i).o + " " + game.exercises.get(i).y;
            exerciseSt = exerciseSt.replace("*","x");
            exercise.setText(exerciseSt);
            equalTo.setText(" = " + game.exercises.get(i).z);


            if (game.exercises.get(i).solve() == game.exercises.get(i).z) {
                solution.setText("\u2713");
                solution.setTextColor(getResources().getColor(R.color.green));
                equalTo.setTextColor(getResources().getColor(R.color.green));
            } else {
                equalTo.setTextColor(getResources().getColor(R.color.red));
                solution.setText("" + game.exercises.get(i).solve());
            }

            exercise.setId(i);


            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.exercises);
            LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.solutions);
            LinearLayout linearLayoutEqualTo = (LinearLayout) findViewById(R.id.equalTo);


            linearLayout.addView(exercise);
            linearLayoutEqualTo.addView(equalTo);
            linearLayout2.addView(solution);



      }

        if(playerName !=null || ! playerName.isEmpty())
            playerName = playerName + " ";
        else
            playerName = "";


          solved.setText(playerName +   "Solved " + game.answeredCorrectly() + " "+ getResources().getString(R.string.result_solved_of) + " 10");



    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



}


