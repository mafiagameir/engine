/*
 *  Copyright (C) 2015 mafiagame.ir
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.engine.command;

import co.mafiagame.common.Constants;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.Role;
import co.mafiagame.engine.exception.CommandIsUnavailableHereException;
import co.mafiagame.engine.util.RoleUtil;
import org.springframework.stereotype.Component;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class WhatIsMyRoleCommand implements Command<EmptyContext> {
    @Override
    public ResultMessage execute(EmptyContext context) {
        validateGameNotNull(context);
        Game game = context.getGame();
        if (context.getInterfaceContext().getSenderType() != ChannelType.USER_PRIVATE)
            throw new CommandIsUnavailableHereException();
        game.update();
        Role role = game.playerByUsername(context.getInterfaceContext().getUserName()).getRole();
        return new ResultMessage(new Message(RoleUtil.roleIs(role))
                .setReceiverId(context.getInterfaceContext().getUserId()),
                ChannelType.USER_PRIVATE, context.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.WHAT_IS_MY_ROLE;
    }
}
