/*
 * Copyright (C) 2015 mafiagame.ir
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.engine.domain;

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.engine.exception.PlayerAlreadyRegisteredException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hekmatof
 */
public class StashedGame {
    private final InterfaceContext interfaceContext;
    private final GameState gameState;
    private final List<Player> players = new ArrayList<>();

    public StashedGame(InterfaceContext interfaceContext, int citizenNum,
                       int mafiaNum, int detectorNum, int doctorNum) {
        this.interfaceContext = interfaceContext;
        gameState = new GameState(citizenNum, mafiaNum, detectorNum, doctorNum);
    }

    public InterfaceContext getInterfaceContext() {
        return interfaceContext;
    }

    public boolean register(Player player) {
        if (players.contains(player))
            throw new PlayerAlreadyRegisteredException();
        players.add(player);
        return gameState.totalPlayer() == players.size();

    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
