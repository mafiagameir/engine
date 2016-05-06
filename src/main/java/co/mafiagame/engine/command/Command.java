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

package co.mafiagame.engine.command;

import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.context.CommandContext;
import co.mafiagame.engine.exception.GameNotStartedException;

import java.util.Objects;

/**
 * base class for all game commands
 *
 * @author hekmatof
 */
public interface Command<T extends CommandContext> {
    default void validateGameNotNull(T context) {
        if (context.getGame() == null)
            throw new GameNotStartedException();
    }

    ResultMessage execute(T context) throws Exception;

    default ResultMessage run(T context) throws Exception {
        if (Objects.nonNull(context.getGame()))
            synchronized (context.getGame()) {
                return execute(context);
            }
        else
            return execute(context);
    }

    String commandName();
}
