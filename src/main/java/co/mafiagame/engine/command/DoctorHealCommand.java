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
import co.mafiagame.engine.command.context.DoctorHealCommandContext;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.GameMood;
import co.mafiagame.engine.domain.Role;
import co.mafiagame.engine.exception.NotInDoctorPrivateException;
import co.mafiagame.engine.exception.NotTimeOfDoctorHeal;
import co.mafiagame.engine.exception.YouAreNotDoctorException;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author nazila
 */
@Component
public class DoctorHealCommand implements Command<DoctorHealCommandContext> {

    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(DoctorHealCommandContext context) {
        validateGameNotNull(context);
        Game game = context.getGame();
        game.update();
        validate(context);
        game.setHealCandidate(game.playerByUsername(context.getHealedName()));
        commandExecutor.run(context.getInterfaceContext(),
                Constants.CMD.Internal.NEXT_MOOD,
                new EmptyContext(context.getInterfaceContext(), game));
        return new ResultMessage(new Message("ok")
                .setReceiverId(game.doctor().getAccount().getUserInterfaceId()),
                context.getInterfaceContext().getSenderType(),
                context.getInterfaceContext());
    }

    private void validate(DoctorHealCommandContext context) {
        Game game = context.getGame();
        if (context.getInterfaceContext().getSenderType() != ChannelType.USER_PRIVATE)
            throw new NotInDoctorPrivateException();
        if (game.playerByUsername(context.getDoctorName()).getRole() != Role.DOCTOR)
            throw new YouAreNotDoctorException();
        if (game.getGameMood() != GameMood.NIGHT_DOCTOR)
            throw new NotTimeOfDoctorHeal();
    }

    @Override
    public String commandName() {
        return Constants.CMD.DOCTOR_HEAL;
    }

}
