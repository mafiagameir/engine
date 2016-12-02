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

package co.mafiagame.engine.command.internalcommand;

import co.mafiagame.common.Constants;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.engine.command.Command;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.command.internalcommand.context.ElectionFinishedCommandContext;
import co.mafiagame.engine.domain.ElectionMood;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class EndElectionOnTimeCommand implements Command<EmptyContext> {
    @Autowired
    private CommandExecutor commandExecutor;
    private ScheduledThreadPoolExecutor executor;

    @PostConstruct
    public void init() {
        executor = new ScheduledThreadPoolExecutor(10);
    }

    @Override
    public ResultMessage execute(EmptyContext context) throws Exception {
        executor.schedule(() -> {
            if (context.getGame().getElectionMood() != ElectionMood.NONE)
                commandExecutor.run(context.getInterfaceContext(),
                        Constants.CMD.Internal.ELECTION_FINISHED, new ElectionFinishedCommandContext(
                                context.getInterfaceContext(), context.getGame(),
                                true));

        }, 2, TimeUnit.MINUTES);
        return new ResultMessage(new Message(""), ChannelType.NONE,
                context.getInterfaceContext());
    }

    @Override
    public String commandName() {
        return Constants.CMD.Internal.END_ELECTION_ON_TIME;
    }
}
