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

package co.mafiagame.engine.command.internalcommand;

import co.mafiagame.common.Constants;
import co.mafiagame.common.channel.InterfaceChannel;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.Command;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.GameResult;
import co.mafiagame.engine.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hekmatof
 */
@Component
public class HandleGameOverCommand implements Command<EmptyContext> {
    @Autowired
    private GameContainer gameContainer;
    @Autowired
    private InterfaceChannel interfaceChannel;

    @Override
    public ResultMessage execute(EmptyContext context) {
        Game game = context.getGame();
        List<Message> messages = new ArrayList<>();
        Map<String, Role> backupPlayerState = game.getBackupPlayerState();
        backupPlayerState.keySet().stream().forEach(
                user -> messages.add(new Message("player.role.was", null,null, user,
                        backupPlayerState.get(user).name())));
        gameContainer.finished(context.getInterfaceContext());
        interfaceChannel.gameOver(game.getBackupPlayerState().keySet());
        GameResult gameResult = game.checkGameOver();
        if (gameResult == GameResult.CITIZEN_WIN)
            messages.add(new Message("citizens.win", null, null));
        else if (gameResult == GameResult.CANCELED)
            messages.add(new Message("game.canceled", null, null));
        else
            messages.add(new Message("mafia.win", null, null));
        return new ResultMessage(messages, ChannelType.GENERAL, context.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.Internal.HANDLE_GAME_OVER;
    }
}
