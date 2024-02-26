package server;

import model.GameData;

import java.util.Collection;

public record GameListRes(Collection <GameData> games) {
}
