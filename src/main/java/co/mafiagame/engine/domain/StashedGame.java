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
import java.util.Date;
import java.util.List;

/**
 * @author hekmatof
 */
public class StashedGame implements InterfaceContextAware {
    private InterfaceContext interfaceContext;
    private GameState gameState;
    private List<Player> players = new ArrayList<>();
    private Date createdDate;
    private Date lastUpdate;

    public StashedGame() {
    }

    public StashedGame(InterfaceContext interfaceContext, int citizenNum,
                       int mafiaNum, int detectorNum, int doctorNum) {
        this.createdDate = new Date();
        this.lastUpdate = new Date();
        this.interfaceContext = interfaceContext;
        gameState = new GameState(citizenNum, mafiaNum, detectorNum, doctorNum);
    }

    public synchronized InterfaceContext getInterfaceContext() {
        return interfaceContext;
    }

    public synchronized boolean register(Player player) {
        this.lastUpdate = new Date();
        if (players.contains(player))
            throw new PlayerAlreadyRegisteredException();
        players.add(player);
        return gameState.totalPlayer() == players.size();

    }

    public synchronized GameState getGameState() {
        return gameState;
    }

    public synchronized List<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "StashedGame{" +
                "interfaceContext=" + interfaceContext +
                ", gameState=" + gameState +
                ", players=" + players +
                ", createdDate=" + createdDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
