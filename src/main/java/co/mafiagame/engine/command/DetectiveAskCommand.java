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
import co.mafiagame.engine.command.context.DetectiveAskCommandContext;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.GameMood;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.domain.Role;
import co.mafiagame.engine.exception.CommandIsUnavailableHereException;
import co.mafiagame.engine.exception.YouAreNotDetectiveException;
import co.mafiagame.engine.exception.YouCantAskNow;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hekmatof
 * @author nazila
 */
@Component
public class DetectiveAskCommand implements Command<DetectiveAskCommandContext> {

    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(DetectiveAskCommandContext context) {
        validateGameNotNull(context);
        Game game = context.getGame();
        game.update();
        Player detective = game.playerByUsername(context.getUsername());
        validate(context);
        Player who = game.playerByUsername(context.getWho());
        commandExecutor.run(context.getInterfaceContext(),
                Constants.CMD.Internal.NEXT_MOOD,
                new EmptyContext(context.getInterfaceContext(), game));
        Message message;
        if (who.getRole() == Role.MAFIA)
            message = new Message("user.role.is.mafia")
                    .setReceiverId(detective.getAccount().getUserInterfaceId())
                    .setArgs(who.getAccount().getUsername());
        else
            message = new Message("user.role.is.not.mafia")
                    .setReceiverId(detective.getAccount().getUserInterfaceId())
                    .setArgs(who.getAccount().getUsername());
        return new ResultMessage(message, ChannelType.USER_PRIVATE,
                context.getInterfaceContext());
    }

    private void validate(DetectiveAskCommandContext context) {
        Game game = context.getGame();
        game.update();
        Player detective = game.playerByUsername(context.getUsername());
        if (context.getInterfaceContext().getSenderType() != ChannelType.USER_PRIVATE)
            throw new CommandIsUnavailableHereException();
        if (detective.getRole() != Role.DETECTIVE)
            throw new YouAreNotDetectiveException();
        if (game.getGameMood() != GameMood.NIGHT_DETECTIVE)
            throw new YouCantAskNow();
    }

    @Override
    public String commandName() {
        return Constants.CMD.DETECTIVE_ASK;
    }

}
