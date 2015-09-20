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

package co.mafiagame.engine.command.internalcommand.context;

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.engine.command.context.CommandContext;
import co.mafiagame.engine.domain.StashedGame;

/**
 * @author nazila
 */
public class StartGameCommandContext extends CommandContext {

    private final StashedGame stashedGame;

    public StartGameCommandContext(InterfaceContext interfaceContext,
                                   StashedGame stashedGame) {
        super(interfaceContext, null);
        this.stashedGame = stashedGame;
    }

    public StashedGame getStashedGame() {
        return stashedGame;
    }

    @Override
    public String toString() {
        return "StartGameCommandContext{" +
                "stashedGame=" + stashedGame +
                '}';
    }
}
