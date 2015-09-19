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

package co.mafiagame.engine.container;

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.engine.domain.StashedGame;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hekmatof
 */
@Component
public class StashedGameContainer {
    private final Map<InterfaceContext, StashedGame> games = new HashMap<>();

    public void addGame(StashedGame game) {
        if (games.get(game.getInterfaceContext()) != null) {
            games.remove(game.getInterfaceContext());
        }
        games.put(game.getInterfaceContext(), game);
    }

    public StashedGame getGame(InterfaceContext interfaceContext) {
        return games.get(interfaceContext);
    }

    public void removeGame(StashedGame game) {
        games.remove(game.getInterfaceContext());
    }

    public Collection<StashedGame> getGames() {
        return games.values();
    }
}
