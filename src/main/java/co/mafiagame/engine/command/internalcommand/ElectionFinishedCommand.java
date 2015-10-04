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
import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.result.ChannelType;
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.common.utils.ListToString;
import co.mafiagame.engine.command.ElectionHandlerCommand;
import co.mafiagame.engine.command.context.EmptyContext;
import co.mafiagame.engine.domain.ElectionMood;
import co.mafiagame.engine.domain.Game;
import co.mafiagame.engine.domain.Player;
import co.mafiagame.engine.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hekmatof
 * @author nazila
 */
@Component
public class ElectionFinishedCommand extends
        ElectionHandlerCommand<EmptyContext> {

    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(EmptyContext context) {
        List<Message> messages = new ArrayList<>();
        Game game = context.getGame();
        electionResult(messages, game);
        if (game.getElectionMood() == ElectionMood.FINALELECTION) {
            boolean electionOver = citizenFinalElectionHandler(messages, game);
            if (electionOver) {
                messages.add(new Message("night.started.be.silent", null, null));
                commandExecutor.run(context.getInterfaceContext(),
                        Constants.CMD.Internal.NEXT_MOOD, new EmptyContext(
                                context.getInterfaceContext(), game));
            }
        }
        game.clearElection();
        return new ResultMessage(messages, ChannelType.GENERAL, context.getInterfaceContext());
    }

    private boolean citizenFinalElectionHandler(List<Message> messages,
                                                Game game) {
        Map<Player, List<Player>> votes = game.getPlayerVote();
        List<Player> usersEqualMaxVote = findMaxVoted(game, false);
        if (usersEqualMaxVote == null) {
            game.clearElection();
            messages.add(new Message("nobody.was.killed.with.maximum.votes", null, null));
            return true;
        } else {
            Player maxVoted = usersEqualMaxVote.get(0);
            int maxVoteNum = votes.get(maxVoted).size();
            if (usersEqualMaxVote.size() == 1) {
                game.killPlayer(maxVoted);

                game.clearElection();
                messages.add(new Message("player.was.killed.with.maximum.votes",
                        null,null, maxVoted.getAccount().getUsername(), String.valueOf(maxVoteNum)));
                return true;
            } else {
                messages.add(new Message(
                        "nobody.was.killed.because.more.than.one.user.has.equal.vote",
                        null,null, ListToString.toString(usersEqualMaxVote.stream().map(Player::getAccount)
                        .map(Account::getUsername).collect(Collectors.toList())),
                        String.valueOf(maxVoteNum)));
                return false;
            }
        }
    }

    @Override
    public String commandName() {
        return Constants.CMD.Internal.ELECTION_FINISHED;
    }

}
