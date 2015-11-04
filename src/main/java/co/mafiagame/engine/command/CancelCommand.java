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

import co.mafiagame.common.Constants;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.context.CancelCommandContext;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.GameResult;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author nazila
 */
@Component
public class CancelCommand implements Command<CancelCommandContext> {

    @Autowired
    CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(CancelCommandContext context) {
        validateGameNotNull(context);
        Game game = context.getGame();
        game.update();
        Player cancelPlayer = game.getPlayerByUsername(context.getUsername());
        game.getCancelPlayers().add(cancelPlayer);
        if (game.checkGameOver() == GameResult.CANCELED) {
            commandExecutor.run(
                    context.getInterfaceContext(),
                    Constants.CMD.Internal.HANDLE_GAME_OVER,
                    new EmptyContext(context.getInterfaceContext(), game)
            );
        }
        return new ResultMessage(new Message("user.canceled.game", null,null,
                context.getUsername()), ChannelType.GENERAL, context.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.CANCEL;
    }

}
