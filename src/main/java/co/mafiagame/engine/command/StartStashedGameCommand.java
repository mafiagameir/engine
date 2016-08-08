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
import co.mafiagame.common.domain.result.Option;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.context.StartStashedGameCommandContext;
import co.mafiagame.engine.container.GameContainer;
import co.mafiagame.engine.container.StashedGameContainer;
import co.mafiagame.engine.domain.StashedGame;
import co.mafiagame.engine.exception.GameAlreadyStartedException;
import co.mafiagame.engine.exception.MoreThanOneDetectiveException;
import co.mafiagame.engine.exception.MoreThanOneDoctorException;
import co.mafiagame.engine.exception.ZeroMafiaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author hekmatof
 */
@Component
public class StartStashedGameCommand implements Command<StartStashedGameCommandContext> {

    @Autowired
    private StashedGameContainer stashedGameContainer;

    @Autowired
    private GameContainer gameContainer;

    @Override
    public ResultMessage execute(StartStashedGameCommandContext context) {
        if (context.getDetectiveNum() > 1)
            throw new MoreThanOneDetectiveException();
        if (context.getDoctorNum() > 1)
            throw new MoreThanOneDoctorException();
        if (context.getMafiaNum() < 1)
            throw new ZeroMafiaException();
        if (gameContainer.getGame(context.getInterfaceContext()) != null)
            throw new GameAlreadyStartedException();
        StashedGame stashedGame = new StashedGame(
                context.getInterfaceContext(), context.getCitizenNum(),
                context.getMafiaNum(), context.getDetectiveNum(),
                context.getDoctorNum());
        stashedGameContainer.addGame(stashedGame);
        int sum = context.getCitizenNum() + context.getMafiaNum() + context.getDetectiveNum() + context.getDoctorNum();
        return new ResultMessage(new Message("stashed.game.started", null, null,
                Collections.singletonList(new Option(Constants.CMD.REGISTER)), null,
                String.valueOf(context.getCitizenNum()),
                String.valueOf(context.getMafiaNum()),
                String.valueOf(context.getDetectiveNum()),
                String.valueOf(context.getDoctorNum()),
                String.valueOf(sum)),
                ChannelType.GENERAL, context.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.START_STASHED_GAME;
    }

}
