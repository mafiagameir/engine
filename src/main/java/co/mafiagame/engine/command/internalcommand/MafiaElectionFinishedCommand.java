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
import co.mafiagame.common.domain.result.Message;
import co.mafiagame.common.domain.result.ResultMessage;
import co.mafiagame.common.utils.ListToString;
import co.mafiagame.engine.command.ElectionHandlerCommand;
import co.mafiagame.engine.command.context.EmptyContext;
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
 * @author nazila
 */
@Component
public class MafiaElectionFinishedCommand extends
        ElectionHandlerCommand<EmptyContext> {

    @Autowired
    private CommandExecutor commandExecutor;

    @Override
    public ResultMessage execute(EmptyContext context) {
        List<Message> messages = new ArrayList<>();
        Game game = context.getGame();
        boolean electionOver = mafiaFinalElectionHandler(messages, game);
        electionResult(messages, game);

        if (electionOver) {
            game.getMafias().stream().map(Player::getAccount)
                    .map(mafia -> new Message("detector.night.started.be.silent",
                            mafia.getUserInterfaceId(), mafia.getUsername()))
                    .forEach(messages::add);

            commandExecutor.run(context.getInterfaceContext(),
                    Constants.CMD.Internal.NEXT_MOOD,
                    new EmptyContext(context.getInterfaceContext(), game));
        }
        return new ResultMessage(
                messages, context.getInterfaceContext().getSenderType(), context.getInterfaceContext());
    }

    private boolean mafiaFinalElectionHandler(List<Message> messages, Game game) {
        Map<Player, List<Player>> votes = game.getPlayerVote();
        List<Player> usersEqualMaxVote = findMaxVoted(game, true);
        if (usersEqualMaxVote == null) {
            game.clearElection();
            game.getMafias().forEach(p ->
                    messages.add(new Message("you.kill.nobody",
                            p.getAccount().getUserInterfaceId(),
                            p.getAccount().getUsername())));
            return true;
        } else {
            Player maxVoted = usersEqualMaxVote.get(0);
            if (usersEqualMaxVote.size() == 1) {
                game.temporaryKillPlayer(maxVoted);
                game.clearElection();
                game.getMafias().forEach(p ->
                        messages.add(new Message("you.kill.a.player",
                                p.getAccount().getUserInterfaceId(), p.getAccount().getUsername(), maxVoted.getAccount().getUsername())));
                return true;
            } else {
                int maxVoteNum = votes.get(maxVoted).size();
                game.getMafias().forEach(p ->
                        messages.add(new Message(
                                "you.cant.decide.who.to.kill",
                                p.getAccount().getUserInterfaceId(),
                                p.getAccount().getUsername(),
                                ListToString.toString(
                                        usersEqualMaxVote.stream()
                                                .map(Player::getAccount)
                                                .map(Account::getUsername)
                                                .collect(Collectors.toList())
                                ), String.valueOf(maxVoteNum))));
                return false;
            }
        }
    }

    @Override
    public String commandName() {
        return Constants.CMD.Internal.MAFIA_ELECTION_FINISHED;
    }

}
