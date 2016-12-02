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
import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.domain.ElectionMood;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.exception.ElectionAlreadyStarted;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hekmatof
 */
@Component
public class StartElectionCommand implements Command<EmptyContext> {
    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(EmptyContext context) {
        validateGameNotNull(context);
        Game game = context.getGame();
        game.update();
        if (game.getElectionMood() != ElectionMood.NONE)
            throw new ElectionAlreadyStarted();
        game.startElection(false);
        List<String> users = game.getPlayers().stream()
                .map(Player::getAccount)
                .map(Account::getUsername)
                .collect(Collectors.toList());
        commandExecutor.run(context.getInterfaceContext(),
                Constants.CMD.Internal.END_ELECTION_ON_TIME,
                new EmptyContext(context.getInterfaceContext(), game));
        return new ResultMessage(
                new Message("election.started")
                        .setOptions(game.makeOption(Constants.CMD.VOTE, true))
                        .setToUsers(users),
                ChannelType.GENERAL, context.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.START_ELECTION;
    }

}
