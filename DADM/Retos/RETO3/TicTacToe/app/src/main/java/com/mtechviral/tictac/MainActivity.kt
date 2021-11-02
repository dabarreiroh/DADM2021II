package com.mtechviral.tictac

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buClick(view: View){
        val buSelected = view as Button
        var cellID = 0
        when(buSelected.id){
            R.id.bu1 -> cellID = 1
            R.id.bu2 -> cellID = 2
            R.id.bu3 -> cellID = 3
            R.id.bu4 -> cellID = 4
            R.id.bu5 -> cellID = 5
            R.id.bu6 -> cellID = 6
            R.id.bu7 -> cellID = 7
            R.id.bu8 -> cellID = 8
            R.id.bu9 -> cellID = 9
        }
        playGame(cellID,buSelected)
    }

    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()

    var activePlayer = 1

    private fun playGame(cellID: Int, buSelected: Button) {
        if(activePlayer==1){
            buSelected.text = "X"
            buSelected.setBackgroundColor(Color.parseColor("#009193"))
            player1.add(cellID)
            activePlayer = 2
            AutoPlay()
        }else{
            buSelected.text = "o"
            buSelected.setBackgroundColor(Color.parseColor("#FF9300"))
            player2.add(cellID)
            activePlayer = 1
        }
        buSelected.isEnabled = false;
        checkWinner()
    }

     fun restartGame(view: View){
        val restartButton = view as Button

         player1.clear()
         player2.clear()

         activePlayer = 1

         bu1.text = ""
         bu1.isEnabled = true
         bu1.setBackgroundColor(Color.parseColor("#FFFFFF"))
         bu2.text = ""
         bu2.isEnabled = true
         bu2.setBackgroundColor(Color.parseColor("#FFFFFF"))
         bu3.text = ""
         bu3.isEnabled = true
         bu3.setBackgroundColor(Color.parseColor("#FFFFFF"))
         bu4.text = ""
         bu4.isEnabled = true
         bu4.setBackgroundColor(Color.parseColor("#FFFFFF"))
         bu5.text = ""
         bu5.isEnabled = true
         bu5.setBackgroundColor(Color.parseColor("#FFFFFF"))
         bu6.text = ""
         bu6.isEnabled = true
         bu6.setBackgroundColor(Color.parseColor("#FFFFFF"))
         bu7.text = ""
         bu7.isEnabled = true
         bu7.setBackgroundColor(Color.parseColor("#FFFFFF"))
         bu8.text = ""
         bu8.isEnabled = true
         bu8.setBackgroundColor(Color.parseColor("#FFFFFF"))
         bu9.text = ""
         bu9.isEnabled = true
         bu9.setBackgroundColor(Color.parseColor("#FFFFFF"))


    }

    private fun checkWinner() {
        var winner = -1
        if(player1.contains(1) && player1.contains(2) && player1.contains(3)){
            winner=1
        }
        if(player2.contains(1) && player2.contains(2) && player2.contains(3)){
            winner=2
        }

        // row 2
        if(player1.contains(4) && player1.contains(5) && player1.contains(6)){
            winner=1
        }
        if(player2.contains(4) && player2.contains(5) && player2.contains(6)){
            winner=2
        }

        // row 3
        if(player1.contains(7) && player1.contains(8) && player1.contains(9)){
            winner=1
        }
        if(player2.contains(7) && player2.contains(8) && player2.contains(9)){
            winner=2
        }

        // col 1
        if(player1.contains(1) && player1.contains(4) && player1.contains(7)){
            winner=1
        }
        if(player2.contains(1) && player2.contains(4) && player2.contains(7)){
            winner=2
        }

        // col 2
        if(player1.contains(2) && player1.contains(5) && player1.contains(8)){
            winner=1
        }
        if(player2.contains(2) && player2.contains(5) && player2.contains(8)){
            winner=2
        }

        // col 3
        if(player1.contains(3) && player1.contains(6) && player1.contains(9)){
            winner=1
        }
        if(player2.contains(3) && player2.contains(6) && player2.contains(9)){
            winner=2
        }

        //diagonal
        if(player1.contains(1) && player1.contains(5) && player1.contains(9)){
            winner=1
        }
        if(player2.contains(1) && player2.contains(5) && player2.contains(9)){
            winner=2
        }

        if(player1.contains(3) && player1.contains(5) && player1.contains(7)){
            winner=1
        }
        if(player2.contains(3) && player2.contains(5) && player2.contains(7)){
            winner=2
        }

        if( winner != -1){
            if (winner==1){
                Toast.makeText(this," Player 1  win the game", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this," Player 2  win the game", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun AutoPlay() {
        val emptyCells=ArrayList<Int>()
        for (cellID in 1..9){
            if(!(player1.contains(cellID) || player2.contains(cellID))){
                emptyCells.add(cellID)
            }
        }

        val r = Random()
        val randIndex = r.nextInt(emptyCells.size-0)+0
        val cellID = emptyCells[randIndex]

        var buSelected:Button
        when(cellID){
            1->buSelected=bu1
            2-> buSelected=bu2
            3-> buSelected=bu3
            4-> buSelected=bu4
            5-> buSelected=bu5
            6-> buSelected=bu6
            7-> buSelected=bu7
            8-> buSelected=bu8
            9-> buSelected=bu9
            else -> buSelected = bu1

        }

        playGame(cellID,buSelected)

    }
}
