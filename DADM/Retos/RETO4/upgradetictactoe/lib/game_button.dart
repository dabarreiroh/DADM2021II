import 'package:flutter/material.dart';

class GameButton {
  final id;
  String text;
  var bg;
  bool enabled;

  GameButton(
      {this.id, this.text = "", this.bg = Colors.grey, this.enabled = true});
}
