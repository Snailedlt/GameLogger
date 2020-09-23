package com.example.profilside.ui.mygamelist

class Game(var title: String, var platform: String, var releaseYear: Int, var state: GameState) {

    companion object {
        fun makeGameList(): ArrayList<Game> {
            val gameList = ArrayList<Game>()
            for (i in 0..15) {
                gameList.add(Game(
                    "Hades",
                    "PC",
                    2020,
                    GameState.PLAYING
                ))
                gameList.add(Game(
                    "Bloodborne",
                    "PS4",
                    2015,
                    GameState.DONE
                ))
            }
            return gameList
        }
    }
}

enum class GameState {
    DONE, PLAYING, INBACKLOG
}