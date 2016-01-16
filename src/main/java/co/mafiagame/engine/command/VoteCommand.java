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
import co.mafiagame.common.utils.ListToString;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.command.context.VoteCommandContext;
import co.mafiagame.engine.domain.ElectionMood;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.GameMood;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.exception.NoElectionStartedException;
import co.mafiagame.engine.exception.VoteOnNightException;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author nazila
 */
@Component
public class VoteCommand extends VotableCommand<VoteCommandContext> {

    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(VoteCommandContext context) {
        validateGameNotNull(context);
        Game game = context.getGame();
        game.update();
        if (game.getGameMood() != GameMood.DAY)
            throw new VoteOnNightException();
        if (game.getElectionMood() == ElectionMood.NONE)
            throw new NoElectionStartedException();
        for (String voted : context.getVotedUsername()) {
            Player voter = game.playerByUsername(context.getVoterUsername());
            vote(voter, voted, game);
            voter.setVoted(true);
        }
        if (game.checkElectionIsOver())
            commandExecutor.run(context.getInterfaceContext(),
                    Constants.CMD.Internal.ELECTION_FINISHED, new EmptyContext(
                            context.getInterfaceContext(), game));
        if (context.getVotedUsername().size() == 1
                && Constants.NO_BODY.equals(context.getVotedUsername().get(0))) {
            return new ResultMessage(new Message("user.vote.nobody", null,
                    context.getVoterUsername(), context.getVoterUsername()), ChannelType.GENERAL,
                    context.getInterfaceContext());
        } else {

            return new ResultMessage(new Message("user.vote.another", null, null,
                    context.getVoterUsername(), ListToString.toString(context.getVotedUsername())),
                    ChannelType.GENERAL,
                    context.getInterfaceContext());
        }
    }

    @Override
    public String commandName() {
        return Constants.CMD.VOTE;
    }

}
