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
import co.mafiagame.engine.command.context.RegisterCommandContext;
import co.mafiagame.engine.command.internalcommand.context.StartGameCommandContext;
import co.mafiagame.engine.container.StashedGameContainer;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.domain.StashedGame;
import co.mafiagame.engine.exception.RegisterBeforeStartException;
import co.mafiagame.engine.executor.CommandExecutor;
import co.mafiagame.persistence.api.PersistenceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hekmatof
 */
@Component
public class RegisterCommand implements Command<RegisterCommandContext> {
    @Autowired
    private PersistenceApi persistenceApi;
    @Autowired
    private StashedGameContainer stashedGameContainer;
    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(RegisterCommandContext context) {
        Account account = persistenceApi.saveAccount(
                context.getUsername(),
                context.getFirstName(),
                context.getLastName(),
                context.getInterfaceContext().interfaceType(),
                context.getInterfaceContext().getUserId());
        StashedGame game = stashedGameContainer.getGame(context.getInterfaceContext());
        if (game == null)
            throw new RegisterBeforeStartException();
        if (game.register(new Player(account)))
            commandExecutor.run(context.getInterfaceContext(),
                    Constants.CMD.Internal.START_GAME,
                    new StartGameCommandContext(context.getInterfaceContext(), game));
        return new ResultMessage(new Message("player.successfully.registered", null, null,
                account.getUsername()), ChannelType.GENERAL, context.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.REGISTER;
    }

}
