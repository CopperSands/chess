package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    //may have to edit because records do not have setter and there is a potential for ChessGame game to change.
}
