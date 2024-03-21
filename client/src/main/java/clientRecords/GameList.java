package clientRecords;

import model.GameData;

import java.util.Collection;

public record GameList(Collection<GameData> games) {
}
