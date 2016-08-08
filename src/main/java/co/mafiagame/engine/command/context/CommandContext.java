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

package co.mafiagame.engine.command.context;

import co.mafiagame.common.channel.InterfaceContext;
import co.mafiagame.engine.domain.Game;

/**
 * @author hekmatof
 */
public abstract class CommandContext {
    private final InterfaceContext interfaceContext;
    private final Game game;

    protected CommandContext(InterfaceContext interfaceContext, Game game) {
        this.interfaceContext = interfaceContext;
        this.game = game;
    }

    CommandContext(InterfaceContext interfaceContext) {
        this.interfaceContext = interfaceContext;
        this.game = null;
    }

    public InterfaceContext getInterfaceContext() {
        return interfaceContext;
    }

    public Game getGame() {
        return game;
    }


}
