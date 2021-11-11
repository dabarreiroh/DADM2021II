import 'dart:math';
import 'package:flutter/material.dart';
import 'package:upgradetictactoe/custom_dailog.dart';
import 'package:upgradetictactoe/game_button.dart';


class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => new _HomePageState();
}

class _HomePageState extends State<HomePage> {
  List<GameButton> buttonsList;
  var player1;
  var player2;
  var activePlayer;

  var _result = 'Easy';
  var _difficultly;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    buttonsList = doInit();
  }

  List<GameButton> doInit() {
    player1 = new List();
    player2 = new List();
    activePlayer = 1;

    var gameButtons = <GameButton>[
      new GameButton(id: 1),
      new GameButton(id: 2),
      new GameButton(id: 3),
      new GameButton(id: 4),
      new GameButton(id: 5),
      new GameButton(id: 6),
      new GameButton(id: 7),
      new GameButton(id: 8),
      new GameButton(id: 9),
    ];
    return gameButtons;
  }

  void playGame(GameButton gb) {
    setState(() {
      if (activePlayer == 1) {
        gb.text = "X";
        gb.bg = Colors.blueAccent;
        activePlayer = 2;
        player1.add(gb.id);
      } else {
        gb.text = "0";
        gb.bg = Colors.green;
        activePlayer = 1;
        player2.add(gb.id);
      }
      gb.enabled = false;
      int winner = checkWinner();
      if (winner == -1) {
        if (buttonsList.every((p) => p.text != "")) {
          showDialog(
              context: context,
              builder: (_) => new CustomDialog("Game Tied",
                  "Press the reset button to start again.", resetGame));
        } else {
          activePlayer == 2 ? autoPlay() : null;
        }
      }
    });
  }

  void autoPlay() {
    var emptyCells = new List();
    var list = new List.generate(9, (i) => i + 1);
    for (var cellID in list) {
      if (!(player1.contains(cellID) || player2.contains(cellID))) {
        emptyCells.add(cellID);
      }
    }

    var r = new Random();
    var randIndex = r.nextInt(emptyCells.length - 1);
    var cellID = emptyCells[randIndex];
    int i = buttonsList.indexWhere((p) => p.id == cellID);
    playGame(buttonsList[i]);
  }

  int checkWinner() {
    var winner = -1;
    if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
      winner = 1;
    }
    if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
      winner = 2;
    }

    // row 2
    if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
      winner = 1;
    }
    if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
      winner = 2;
    }

    // row 3
    if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
      winner = 1;
    }
    if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
      winner = 2;
    }

    // col 1
    if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
      winner = 1;
    }
    if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
      winner = 2;
    }

    // col 2
    if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
      winner = 1;
    }
    if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
      winner = 2;
    }

    // col 3
    if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
      winner = 1;
    }
    if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
      winner = 2;
    }

    //diagonal
    if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
      winner = 1;
    }
    if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
      winner = 2;
    }

    if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
      winner = 1;
    }
    if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
      winner = 2;
    }

    if (winner != -1) {
      if (winner == 1) {
        showDialog(
            context: context,
            builder: (_) => new CustomDialog("Player 1 Won",
                "Press the reset button to start again.", resetGame));
      } else {
        showDialog(
            context: context,
            builder: (_) => new CustomDialog("Player 2 Won",
                "Press the reset button to start again.", resetGame));
      }
    }

    return winner;
  }

  void resetGame() {
    if (Navigator.canPop(context)) Navigator.pop(context);
    setState(() {
      buttonsList = doInit();
    });
  }

  void newGame() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: new Text("Are yo sure you want to start a new game?"),
          actions: <Widget>[
            new FlatButton(
              child: new Text("Regret"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            FlatButton(
              child: new Text("Sure"),
              onPressed: () => resetGame(),
            ),
          ],
        );
      },
    );
  }

  void exitGame() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: new Text("Are yo sure you want to quit?"),
          actions: <Widget>[
            new FlatButton(
              child: new Text("Regret"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            FlatButton(
              child: new Text("Exit"),
              onPressed: () {
                SystemNavigator.pop();
              },
            ),
          ],
        );
      },
    );
  }

  void _handleRadioValueChange(int value) {
    setState(() {
      _difficultly = value;
      switch (_difficultly) {
        case 0:
          _result = 'Easy';
          break;
        case 1:
          _result = 'Medium';
          break;
        case 2:
          _result = 'Hard';
          break;
      }
    });

    Navigator.pop(context);
  }

  void difficultly() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
            title: new Text("Choose the difficulty"),
            actions: <Widget>[
              Container(
                child: Row(
                  children: <Widget>[
                    Text('Easy'),
                    Radio<int>(
                      value: 0,
                      groupValue: _difficultly,
                      onChanged: _handleRadioValueChange,
                    ),
                    Text('Medium'),
                    Radio<int>(
                      value: 1,
                      groupValue: _difficultly,
                      onChanged: _handleRadioValueChange,
                    ),
                    Text('Hard'),
                    Radio<int>(
                      value: 2,
                      groupValue: _difficultly,
                      onChanged: _handleRadioValueChange,
                    ),
                  ],
                ),
              ),
            ]);
      },
    );

  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("Tic Tac Toe"),
        ),
        body: Container(
          margin: EdgeInsets.all(14.0),
          child: new Column(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: <Widget>[
              new Expanded(
                child: new GridView.builder(
                  padding: const EdgeInsets.all(10.0),
                  gridDelegate: new SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 3,
                      childAspectRatio: 1.0,
                      crossAxisSpacing: 9.0,
                      mainAxisSpacing: 9.0),
                  itemCount: buttonsList.length,
                  itemBuilder: (context, i) => new SizedBox(
                    width: 100.0,
                    height: 100.0,
                    child: new RaisedButton(
                      padding: const EdgeInsets.all(8.0),
                      onPressed: buttonsList[i].enabled
                          ? () => playGame(buttonsList[i])
                          : null,
                      child: new Text(
                        buttonsList[i].text,
                        style: new TextStyle(
                            color: Colors.white, fontSize: 20.0),
                      ),
                      color: buttonsList[i].bg,
                      disabledColor: buttonsList[i].bg,
                    ),
                  ),
                ),
              ),
              Center(
                child: Padding(
                    padding: EdgeInsets.all(12.0),
                    child: Text('Difficulty: $_result')),
              ),
              FlatButton(
                child: new Text(
                  "New game",
                  style: new TextStyle(color: Colors.white, fontSize: 20.0),
                ),
                color: Colors.indigo,
                padding: const EdgeInsets.all(20.0),
                onPressed: newGame,
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 8.0),
                child: new FlatButton(
                  child: new Text(
                    "Difficulty",
                    style: new TextStyle(color: Colors.white, fontSize: 20.0),
                  ),
                  color: Colors.orangeAccent,
                  padding: const EdgeInsets.all(20.0),
                  onPressed: difficultly,
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 8.0),
                child: new FlatButton(
                  child: new Text(
                    "Exit",
                    style: new TextStyle(color: Colors.white, fontSize: 20.0),
                  ),
                  color: Colors.red,
                  padding: const EdgeInsets.all(20.0),
                  onPressed: exitGame,
                ),
              ),
            ],
          ),
        ));
  }
}
