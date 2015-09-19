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

package co.mafiagame.engine.api;

import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.container.StashedGameContainer;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.StashedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author hekmatof
 */
@Component
public class ManagementApi {

    @Autowired
    private GameContainer gameContainer;

    @Autowired
    private StashedGameContainer stashedGameContainer;

    public Collection<Game> getGames() {
        return gameContainer.getGames();
    }

    public Collection<StashedGame> getStashedGames() {
        return stashedGameContainer.getGames();
    }
}
